package com.gxh.reentrantLock;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Log")
public class awaitAndSignal {
    static private boolean A;
    static private boolean B;
    static ReentrantLock ROOM = new ReentrantLock();
    // 等待A的休息室
    static Condition waitA = ROOM.newCondition();
    // 等待A的休息室
    static Condition waitB = ROOM.newCondition();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            ROOM.lock();
            try {

                log.info("有A没？[{}]", A);
                if (!A) {
                    log.debug("没A先歇会！");
                    waitA.await();
                }
                log.info("有A没？[{}]", A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ROOM.unlock();
            }
        }, "小南").start();
        new Thread(() -> {
            ROOM.lock();
            try {
                log.info("有B没？[{}]", B);
                if (!B) {
                    log.debug("没B先歇会！");
                    waitB.await();
                }
                log.info("有B没？[{}]", B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ROOM.unlock();
            }
        }, "小女").start();


        Thread.sleep(1000);

        new Thread(()->{
           ROOM.lock();
           try{
               B = true;
               waitB.signal();
               log.debug("B送到了！小女醒来");
           }finally {
               ROOM.unlock();
           }
        }).start();

    }
}
