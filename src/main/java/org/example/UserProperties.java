package org.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: hujingfang
 * @Desc:
 * @create: 2024-07-04 09:38
 **/

@ConfigurationProperties(prefix = "test")
public class UserProperties {


    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
