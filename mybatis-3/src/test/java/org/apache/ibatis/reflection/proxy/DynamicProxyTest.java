package org.apache.ibatis.reflection.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

public class DynamicProxyTest {
    public interface Hello {
        String sayHello(String name);
    }

    @Test
    public void TestDynamicProxy() {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                String hello = null;
                if (method.getName().equals("sayHello")) {
                    hello = "Hello:" + args[0];
                    System.out.println(hello);
                }
                return hello;
            }
        };
        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
        String result = "Hello:Tom";
        String sayHello = hello.sayHello("Tom");
        assertEquals(result,sayHello);
    }
}
