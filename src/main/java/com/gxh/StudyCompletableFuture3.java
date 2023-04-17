package com.gxh;

import java.util.concurrent.*;

public class StudyCompletableFuture3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        System.out.println(CompletableFuture.supplyAsync(()->1).thenRun(()->{}).join());
//        System.out.println(CompletableFuture.supplyAsync(()->1).thenAccept(f->f--).join());
//        System.out.println(CompletableFuture.supplyAsync(()->1).thenApply((f)->f + 100).join());

        CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
            return "playA";
        });
        CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            return "playB";
        });

        CompletableFuture<String> result = playA.applyToEither(playB, f -> f + " is winner");
        System.out.println(result.join());


    }
}
