package com.glide.threadPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TestForkJoin1 {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
        System.out.println(pool.invoke(new MyTask(5)));
    }

}

class MyTask extends RecursiveTask<Integer> {

    private int n;
    public MyTask(int n) {
        this.n = n;
    }
    @Override
    public String toString() {
        return "{" + n + '}';
    }

    @Override
    protected Integer compute() {
        if( n == 1) {
            return 1;
        }
        MyTask t1 = new MyTask(n - 1);
        t1.fork();
        return n + t1.join();
    }
}
