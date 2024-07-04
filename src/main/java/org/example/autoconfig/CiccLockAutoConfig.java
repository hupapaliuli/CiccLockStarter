package org.example.autoconfig;

/**
 * @author: hujingfang
 * @Desc:
 * @create: 2024-07-04 11:19
 **/

//分布式锁  做成starter？  考虑那些东西

//1 .引入redission的包，  核心代码：
/*


String key = TASK_KEY + req.act_id + req.play_id + req.task_id + req.cust_no;
        RLock lock = redissonClient.getLock(key);
        try {
        //表示尝试加锁，立马返回失败OR成功，
        boolean locked = lock.tryLock();
        finally {
        //在并发环境下避免释放其他线程的锁
        if(lock.isHeldByCurrentThread()) {
        lock.unlock();
        }
        }

        redis要注入，才能够有  redissonClient
        得写redis地址，做成配置化


配置：
cicc.redission.ip=
cicc.redission.port=




*/

import io.netty.channel.nio.NioEventLoopGroup;
import org.example.CiccLockProperties;
import org.example.annotation.CiccLock;
import org.example.aspect.CiccLockAspect;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 核心：你暴露给客户端的是什么?  客户端怎么操作？
 *
 * @autowired
 * CiccDistributedLock  ciccLock;
 *
 *
 * lock.tryLock(key);
 *
 *
 * lock.release;
 *
 *
 * 还得要aop    做成注解的形式，  方法上加一个注解   但是如何获取参数上的  eventid 和 custno呢？
 *
 *
 */

@Configuration
@EnableConfigurationProperties(CiccLockProperties.class)
@ConditionalOnProperty(name = "cicc.redission.enabled" , havingValue = "true", matchIfMissing = false)
public class CiccLockAutoConfig {

    @Autowired
    CiccLockProperties ciccLockProperties;

    //注入一个redission template
    @Bean(name = "redissonClient2")
    public RedissonClient redisson() throws Exception {
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + ciccLockProperties.getIp() + ":" + ciccLockProperties.getPort())
                .setTimeout(ciccLockProperties.getTimeout());

        if(!StringUtils.isEmpty(ciccLockProperties.getPassword())) {
            singleServerConfig.setPassword(ciccLockProperties.getPassword());
        }
        Codec codec = (Codec) ClassUtils.forName("org.redisson.codec.JsonJacksonCodec", ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setThreads(20);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }


    @Bean
    public CiccLockAspect ciccLockAspect(){
        return new CiccLockAspect();
    }


}
