package com.glide.死锁;

import lombok.extern.slf4j.Slf4j;

public class ReentrantLock解决哲学家就餐问题 {

    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("c1");
        Chopstick c2 = new Chopstick("c1");
        Chopstick c3 = new Chopstick("c1");
        Chopstick c4 = new Chopstick("c1");
        Chopstick c5 = new Chopstick("c1");
        new Philosopher1("哲学家1", c1, c2).start();
        new Philosopher1("哲学家2", c2, c3).start();
        new Philosopher1("哲学家3", c3, c4).start();
        new Philosopher1("哲学家4", c4, c5).start();
        new Philosopher1("哲学家5", c5, c1).start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher1 extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosopher1(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            if(left.tryLock()){
                try {
                    if(right.tryLock()){
                        try {
                            log.debug("eating ... ");
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }

        }
    }
}
