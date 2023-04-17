package com.gxh.自定义;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
}

class BlockingQueue<T> {
    private LinkedList<T> queue = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition fullWaitSet = lock.newCondition();
    private Condition emptyWaitSet = lock.newCondition();
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    // 带超时的阻塞获取任务
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try{
            long nanos = unit.toNanos(timeout);
            while(queue.isEmpty()){
                try {
                    if(nanos <= 0) return null;
                    // 返回的是剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T res = queue.removeFirst();
            fullWaitSet.signal();
            return res;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞获取任务
    public T take() {
        lock.lock();
        try{
            while(queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T res = queue.removeFirst();
            fullWaitSet.signal();
            return res;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加任务
    public void put(T t) {
        lock.lock();
        try{
            while(queue.size() == capcity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(t);
        }finally {
            lock.unlock();
        }
    }

    public int size(int capcity) {
        lock.lock();
        try{
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

}
