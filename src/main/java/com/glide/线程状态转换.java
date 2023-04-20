package com.glide;

public class 线程状态转换 {
    public static void main(String[] args) throws InterruptedException {

        MyThread myThread = new MyThread();
        myThread.start();
        myThread.join();
        System.out.println("123");
    }
}
class MyThread extends Thread {
    public void run(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
