package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: hujingfang
 * @Desc:  redission  分布式锁
 * @create: 2024-07-04 13:36
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CiccLock {

    /**
     * 锁超时时间
     */
    int timeout() default 200;
}
