package com.glide.threadPool;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestSchedule {
    // 指定一个 时间 定时执行任务
    public static void main(String[] args) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        // 获取当前是当前这周星期几的时间
        LocalDateTime time = now.withHour(20).withMinute(24).withSecond(0).withNano(0).with(DayOfWeek.TUESDAY);
        System.out.println(time);
        if(now.compareTo(time) > 0) {
            // 如果时间小于现在时间，则加一周
            time = time.plusWeeks(1);
            System.out.println(time);
        }
        // 参数2：还要多久才执行
        long initalDelay = Duration.between(now, time).toMillis();
        // 参数3：隔多久执行一次
        long period = 1000;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(()->{
            System.out.println("running");
        },initalDelay, period, TimeUnit.MILLISECONDS);
    }
}
