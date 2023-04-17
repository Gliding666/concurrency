package com.gxh.死锁;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

public class 哲学家就餐 {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("c1");
        Chopstick c2 = new Chopstick("c1");
        Chopstick c3 = new Chopstick("c1");
        Chopstick c4 = new Chopstick("c1");
        Chopstick c5 = new Chopstick("c1");
        new Philosopher("哲学家1", c1, c2).start();
        new Philosopher("哲学家2", c2, c3).start();
        new Philosopher("哲学家3", c3, c4).start();
        new Philosopher("哲学家4", c4, c5).start();
        new Philosopher("哲学家5", c5, c1).start();
    }

}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (left) {
                synchronized (right) {
                    log.debug("eating ... ");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

class Chopstick extends ReentrantLock {
    private String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "name='" + name + '\'' +
                '}';
    }
}