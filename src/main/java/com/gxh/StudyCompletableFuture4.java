package com.gxh;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class StudyCompletableFuture4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> resultA = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
            return 20;
        });
        CompletableFuture<Integer> resultB = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName());
            return 30;
        });
        CompletableFuture<Integer> result = resultB.thenCombine(resultA, (x, y) -> {
            System.out.println(Thread.currentThread().getName());
            return x + y;
        });

        System.out.println(result.join());

        FutureTask futureTask = new FutureTask<>(()->"123");

    }
}
