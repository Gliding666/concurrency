package com.gxh.volatileStudy.doubleCheckedLocking;

// 单例模式：懒汉实现方法之一
public class Singleton {
    private Singleton() {};
    private static volatile Singleton INSTANCE = null;
    // 双重检测可以避免以后每一次获取单列都要使用synchronized，造成不必要的阻塞，影响性能
    // 需要加volatile防止多个线程因为指令重排序，导致读取到错误数据
    public static Singleton getInstance() {
        if(INSTANCE == null){ //
            synchronized (Singleton.class) {
                if(INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }

}
