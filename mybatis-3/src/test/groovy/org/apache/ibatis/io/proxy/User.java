package org.apache.ibatis.io.proxy;

public class User implements UserInterface{

    @Override
    public String sayHello(String name) {
        System.out.println("hello " + name);
        return "OK";
    }
}
