package com.gxh.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "c.TestThreadPoolExecutors")
public class TestThreadPoolExecutors {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "myPool_t" + t.getAndIncrement());
            }
        });

        for(int i = 0; i < 1000; i ++ ) {
            int j = i;
            pool.execute(()->{
                log.debug(("" + (j + 1)));
            });
        }
    }
}
