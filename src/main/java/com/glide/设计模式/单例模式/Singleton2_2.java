package com.glide.设计模式.单例模式;

/**
 * 对 2_1 版本进行改进，当实例对象为null时再去获取锁，生成实例对象
 * 这样减少了开销，但是多线程环境下，依旧存在生成多个实例的问题
 */
public class Singleton2_2 {

    private Singleton2_2(){}

    private static Singleton2_2 instance;

    public static Singleton2_2 getInstance() {
        if(instance == null) {
            synchronized (Singleton2_2.class) {
                instance = new Singleton2_2();
            }
        }
        return instance;
    }
}
