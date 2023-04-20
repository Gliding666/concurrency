package com.glide;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
        });

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程启动了");
            return "hello supplyAsync";
        }, threadPool);

//
//        System.out.println(future.get());
//        System.out.println(supplyAsync.get());

        threadPool.shutdown();
    }
}
