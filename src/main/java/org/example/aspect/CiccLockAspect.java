package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.annotation.CiccLock;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author: hujingfang
 * @Desc:
 * @create: 2024-07-04 13:39
 **/
@Aspect
//@Component
@Slf4j
public class CiccLockAspect {


    @Resource(name = "redissonClient2")
    RedissonClient redissonClient;


    @Around("@annotation(ciccLock)")
    public Object aroundLockService(ProceedingJoinPoint joinPoint, CiccLock ciccLock) throws Throwable {

        Object[] args = joinPoint.getArgs();
        if(args == null || args.length<1 ){
            //加了@CiccLock注解的方法， 方法参数为空的， 我们认为是异常情况， 因为分布式锁一定是加在用户级别上的
            throw new RuntimeException("分布式锁 AOP  方法参数为空");
        }

        Object arg0 = args[0];
        Field custNoField =  arg0.getClass().getDeclaredField("cust_no");
        custNoField.setAccessible(true);
        String lockKey = (String) custNoField.get(arg0);

        RLock lock = redissonClient.getLock(lockKey);


        boolean locked = false;
        Object ret = null;
        try{
            //表示尝试加锁，立马返回失败 OR 成功，
            locked = lock.tryLock();
            if(locked == false){
                log.warn("---------加锁失败，锁key：{}",lockKey);
                throw new RuntimeException("加锁失败锁key："+lockKey);
            }
            //加锁成功  执行方法
            log.info("---------加锁成功1，锁key：{}",lockKey);
            ret = joinPoint.proceed();
        }catch (InterruptedException e){
            log.error("InterruptedException e:",e);
        }finally {
            //最后解锁
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        return ret;
    }


}
