package com.glide;

public class LockCase {
    private static Object resouce1 = new Object(); // 资源1
    private static Object resouce2 = new Object(); // 资源2

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (resouce1) {
                System.out.println(Thread.currentThread() + "我得到了资源1");
                try {
                    Thread.sleep(1000); //休息一会让线程2得到资源2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "我正在等待资源2...");
                synchronized (resouce2) {
                    System.out.println(Thread.currentThread() + "我得到了资源2");
                }
            }
        },"线程 1").start();
        new Thread(() -> {
            synchronized (resouce2) {
                System.out.println(Thread.currentThread() + "我得到了资源2");
                try {
                    Thread.sleep(1000); //休息一会让线程2得到资源2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "我正在等待资源1...");
                synchronized (resouce1) {
                    System.out.println(Thread.currentThread() + "我得到了资源1");
                }
            }
        },"线程 2").start();


    }

}
