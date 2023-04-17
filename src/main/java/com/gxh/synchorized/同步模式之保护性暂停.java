package com.gxh.synchorized;

public class 同步模式之保护性暂停 {
    // 模拟一个场景：线程1 要等待 线程2 的结果
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            Object res = guardedObject.get(2000);
            System.out.println(res);
        },"t1").start();
        new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardedObject.set("123");
        },"t2").start();
    }
}

class GuardedObject {
    private Object response;

    public Object get(long timeout){ // 获得结果
        synchronized (this) {
            long begin = System.currentTimeMillis();
            long now = 0; //经过的时间
            while(response == null) {
                long waitTime = timeout - now;
                if(waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                now = System.currentTimeMillis() - begin;
            }
        }
        return response;
    }

    public void set(Object response) { //产生结果
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }

}
