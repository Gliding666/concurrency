package com.gxh;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new Thread(()-> System.out.println("线程3启动了")).start();
//        new Thread(new MyThread2()).start();
//        new MyThread1().start();

        FutureTask futureTask = new FutureTask<>(new MyThread3());

        new Thread(futureTask, "Callable线程").start();
        System.out.println(futureTask.get());
        Object o = new Object();
    }

}


class MyThread3 implements java.util.concurrent.Callable {

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+"正在执行");
        return 1024;
    }
}

class MyThread1 extends Thread{
    @Override
    public void run() {
        System.out.println("线程1启动了");
    }
}
class MyThread2 implements Runnable {

    @Override
    public void run() {
        System.out.println("线程2启动了");
    }
}
