package com.glide.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

public class TestStampedLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped data = new DataContainerStamped();
        new Thread(() -> {
            data.read(1000);
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            data.write();
        }).start();
    }
}


@Slf4j(topic = "c.DataContainer")
class DataContainerStamped {
    private Object data;
    private final StampedLock lock = new StampedLock();

    public Object read(int readTime) {
        long stamp = lock.tryOptimisticRead();
        log.debug("获取读锁...{}", stamp);
        try {
            Thread.sleep(readTime); // 模拟读取时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(lock.validate(stamp)) {
            log.debug("乐观锁读取到数据... {}", stamp);
            return data;
        }
        // 锁升级 - 读锁
        log.debug("updating to read lock ... {}", stamp);
        try{
            stamp = lock.readLock();
            log.debug("读取...{}",stamp);
            Thread.sleep(readTime);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.debug("释放锁...,{}",stamp);
            lock.unlock(stamp);
        }
        return null;
    }

    public void write() {
        long stamp = lock.writeLock();
        log.debug("获取写锁...{}", stamp);
        try{
            log.debug("写数据...{}",stamp);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("释放锁...{}",stamp);
            lock.unlock(stamp);
        }
    }

}