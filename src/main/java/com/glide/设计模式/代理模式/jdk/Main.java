package com.glide.设计模式.代理模式.jdk;

import com.glide.设计模式.代理模式.SmsService;
import com.glide.设计模式.代理模式.SmsServiceImpl;

public class Main {
    public static void main(String[] args) {

        SmsService smsService = new SmsServiceImpl();

        /**************** 静态代理 ******************/
//        SmsProxy smsProxy = new SmsProxy(smsService);
//        smsProxy.send("java");
        /**************** 动态代理:jdk：版本1 ******************/
//        SmsService jdkSmsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
//        jdkSmsService.send("java");

        /**************** 动态代理:jdk：版本2 ******************/
//        SmsService jdkSmsService1 = (SmsService) new ProxyFactory(smsService, () -> {
//            System.out.println("方法前执行");
//        }, () -> {
//            System.out.println("方法后发送");
//        }).newProxyInstance();
//        jdkSmsService1.send("java");

        /**************** 动态代理:jdk：版本3 ******************/
        SmsService proxy = (SmsService)MyProxy.getProxy(smsService, () -> System.out.println("123"), () -> System.out.println("456"));
        proxy.send("java");

    }
}
