package com.glide.设计模式.单例模式;

/**
 * 虽然懒汉模式变成线程安全的
 * 但由于整个方法都被 synchronized 所包围，每次执行方法都需要获取锁
 * 因此增加了同步开销，降低了程序的执行效率。
 */
public class Singleton2_1 {

    private Singleton2_1(){}

    private static Singleton2_1 instance;

    public synchronized static Singleton2_1 getInstance() {
        if(instance == null) {
            instance = new Singleton2_1();
        }
        return instance;
    }
}
