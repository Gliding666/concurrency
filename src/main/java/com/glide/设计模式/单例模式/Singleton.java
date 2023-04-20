package com.glide.设计模式.单例模式;

// 单例模式：懒汉实现方法之一
public class Singleton {
    private Singleton(){}
    public static class LazyHolder{
        static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }
}
