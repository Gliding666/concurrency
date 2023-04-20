package com.glide;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;

@Slf4j(topic = "c.Test1")
public class Test {
    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        HashSet<Object> objects = new HashSet<>();

        // 创建线程对象
        Thread t = new Thread() {
            public void run() {
                // 要执行的任务

                log.debug("hello");
            }
        };
        // 启动线程
        t.start();
    }
}
