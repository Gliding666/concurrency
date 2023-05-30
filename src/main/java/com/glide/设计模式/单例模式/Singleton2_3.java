package com.glide.设计模式.单例模式;

/**
 * 懒汉式：双层检查锁实现 推荐使用
 */
public class Singleton2_3 {
    /**
     * 为什么用使用volatile修饰？
     * 对象的构造过程：1.分配内存空间 2.初始化对象 3. 将内存地址赋值给引用
     * 但是由于操作系统会对指令进行重排序，所以可能出现分配好空间后，先将内存地址赋值给引用，再初始化对象
     * 这时多线程环境下，其它线程就有可能得到未初始化的对象
     */
    public static volatile Singleton2_3 singleton;
     // 构造函数私有，禁止外部实例化
    private Singleton2_3() {}
    public static Singleton2_3 getInstance() {
        if (singleton == null) {
            synchronized (Singleton2_3.class) {
                if (singleton == null) {
                    singleton = new Singleton2_3();
                }
            }
        }
        return singleton;
    }
}
