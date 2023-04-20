package com.glide.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainer data = new DataContainer();
        new Thread(() -> {
            data.read();
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            data.write();
        }).start();
    }
}


@Slf4j(topic = "c.DataContainer")
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try{
            log.debug("读取");
            Thread.sleep(1000);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.debug("释放锁...");
            r.unlock();
        }
        return null;
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try{
            log.debug("写数据");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("释放锁...");
            w.unlock();
        }
    }

}
