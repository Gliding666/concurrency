package com.gxh.自定义;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestThreadPool")
class TestThreadPool {
    public static void main(String[] args) {
//        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1,
                (queue, task)->{
                    // 死等
                    //queue.put(task);
                    // 带超时等待
                    //queue.offer(task, 1500, TimeUnit.MILLISECONDS);
                    // 让调用者放弃任务执行
//                    log.debug("放弃任务{}", task);
                    // 让调用这抛出异常
//                    throw new RuntimeException("任务执行失败" + task);
                    // 让调用者自己执行任务
                    task.run();
                });
        for(int i = 0; i < 4; i ++ ) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", j);
            });
        }
        log.info("主线程");
    }
}

@FunctionalInterface // 拒绝策略
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j(topic = "c.ThreadPool")
public class ThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;
    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();
    // 核心线程数
    private int coreSize;
    // 获取任务的超时时间
    private long timeout;
    private TimeUnit timeUnit;
    private RejectPolicy<Runnable> rejectPolicy;


    // 执行任务
    public void execute(Runnable task) {
        synchronized (workers) {
            // 当前执行的线程数小于核心数
            if(workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.info("新增 worker {}", worker);
                worker.start();
                workers.add(worker);
            } else {
                // 核心线程满了，先加入阻塞队列
//                log.info("核心线程满都被使用了，先尝试加入阻塞队列{}", task);
//                taskQueue.put(task);
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapcity, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    class Worker extends Thread{
        private Runnable task;
        public Worker(Runnable task) {
            this.task = task;
        }
        public void run() {
            // 执行任务 1.当task不为空，执行任务； 2.当task执行完毕，从阻塞队列获取任务并执行
//            while(task != null || (task = taskQueue.take()) != null) {
            while(task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.info("正在执行...{}", task);
                    task.run();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.info("worker 被移除了{}", this);
                workers.remove(this);
            }
        }

        @Override
        public String toString() {
            return "Worker{" +
                    "task=" + task +
                    '}';
        }
    }
}

@Slf4j(topic = "c.BlockingQueue")
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
    public void put(T task) {
        lock.lock();
        try{
            while(queue.size() == capcity) {
                try {
                    log.info("等待加入阻塞队列,{}", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.info("加入阻塞队列成功,{}", task);
        }finally {
            lock.unlock();
        }
    }

    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try{
            long nanos = timeUnit.toNanos(timeout);
            while(queue.size() == capcity) {
                try {
                    log.info("等待加入阻塞队列,{}", task);
                    if(nanos <= 0) {
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.info("加入阻塞队列成功,{}", task);
        }finally {
            lock.unlock();
        }
        return true;
    }

    public int size(int capcity) {
        lock.lock();
        try{
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    // 根据拒绝策略执行put方法
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否已满？
            if (queue.size() == capcity) {
                try {
                    rejectPolicy.reject(this, task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.debug("加入任务队列{}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
