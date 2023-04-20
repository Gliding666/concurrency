package com.glide.设计模式.代理模式.jdk;

import java.lang.reflect.Proxy;

public class JdkProxyFactory {
    public static Object getProxy(Object target) {
//        return Proxy.newProxyInstance(
//                target.getClass().getClassLoader(), // 目标类的类加载
//                target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
//                new DebugInvocationHandler(target)   // 代理对象对应的自定义 InvocationHandler
//        );
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 目标类的类加载
                target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
                (proxy, method,args) -> {
                    //调用方法之前，我们可以添加自己的操作
                    System.out.println("before method " + method.getName());
                    Object result = method.invoke(target, args);
                    //调用方法之后，我们同样可以添加自己的操作
                    System.out.println("after method " + method.getName());
                    return result;
                }
        );

    }
}
