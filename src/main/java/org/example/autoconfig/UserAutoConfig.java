package org.example.autoconfig;

import org.example.UserProperties;
import org.example.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: hujingfang
 * @Desc:
 * @create: 2024-07-04 09:50
 **/
//@EnableAutoConfiguration
@ConditionalOnClass(UserProperties.class)
//@ConditionalOnProperty(name = "name",havingValue = true)
@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfig {

    @Autowired
    private UserProperties userProperties;

    @Bean
    public UserService userService(){
        System.out.println("--------------auto config------------");
        return new UserService(userProperties.getName(),userProperties.getAge());
    }
}
