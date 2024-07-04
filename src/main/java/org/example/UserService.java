package org.example;

/**
 * @author: hujingfang
 * @Desc:
 * @create: 2024-07-04 09:38
 **/
public class UserService {

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

    public UserService(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
