package com.glide.设计模式.单例模式;

/**
 * 单例模式：通过静态内部类实现懒汉式 推荐使用
 * 静态内部类只有在调用 getInstance() 方法时，才会装载内部类从而完成实例的初始化工作，因此不会造成资源浪费的问题
 * jvm保证只有一个线程加载类，因此不会出现线程安全问题
 */

public class Singleton3 {
    private Singleton3(){}
    public static class LazyHolder{
        static final Singleton3 INSTANCE = new Singleton3();
    }
    public static Singleton3 getInstance() {
        return LazyHolder.INSTANCE;
    }
}
