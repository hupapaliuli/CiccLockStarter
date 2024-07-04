##spring业务服务使用分布式锁三部曲

(1)
**引入分布式锁maven坐标**
`<dependency>
    <groupId>org.example</groupId>
    <artifactId>cicclock-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>`

(2)
**spring服务的配置文件（application.properties 或 nacos配置文件 或统一配置中心等）增加一下配置：**
`cicc.redission.enabled=true
cicc.redission.ip=10.180.41.14
cicc.redission.port=6379
cicc.redission.password=123456
cicc.redission.timeout=6000`

(3)
**具体的java方法上，增加注解 @CiccLock**   
注意：被注解方法的 第一个方法入参必须为实体，且实体类内必须含有cust_no参数，


>仅需三部步骤即可实现无侵入式分布式锁，完全与业务逻辑分离，底层逻辑使用用户号层级 做分布式锁