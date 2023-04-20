package com.glide.synchorized;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j(topic = "c.生产者消费者模式")
public class 生产者消费者模式 {
    // 应用
    public static void main(String[] args) {

        MessageQueue messageQueue = new MessageQueue(2);

        // 3个生产者线程
        for(int i = 0; i < 3; i ++ ) {
            int id = i;
            new Thread(() -> {
                messageQueue.put(new Message(id, "值" + id));
            },"生产者" + id).start();
        }
        // 1个消费者线程
        new Thread(()->{
            while (true) {
                Message message = messageQueue.take();
//                log.info("拿到了一个message");
            }
        },"消费者").start();

    }
}

// 消息队列类 java线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    private int size;
    private LinkedList<Message> messageQueue;

    public MessageQueue(int size){
        this.size = size;
        messageQueue = new LinkedList<>();
    }

    public Message take() {
        synchronized (this) {
            while (messageQueue.isEmpty()) {
                try {
                    log.info("队列为空，进行等待");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message res = messageQueue.removeFirst();
            log.info("拿到了一个message,id：{},值是：{}",res.getId(),res.getMessage());
            this.notifyAll();
            return res;
        }
    }

    public void put(Message message) {
        synchronized (this) {
            while(messageQueue.size() == size){
                log.info("队列满了");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            messageQueue.addLast(message);
            log.info("放入一个message");
            this.notifyAll();
        }
    }

}

class Message{
    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Object getMessage() {
        return message;
    }
}
