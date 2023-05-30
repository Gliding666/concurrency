package com.glide.设计模式.单例模式;

/**
 * 单例模式：懒汉式（简单写法）
 * 多线程环境下是非线程是安全的，可能会生成多个实例
 */
public class Singleton1 {

    private Singleton1() {}

    private static Singleton1 instance;

    public static Singleton1 getInstance(){
        if(instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }
}
