package com.glide.设计模式.代理模式.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyProxy {
    public static Object getProxy(Object target, Runnable before, Runnable after) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        before.run();
                        Object result = method.invoke(target, args);
                        after.run();
                        return result;
                    }
                }
        );
    }
}
