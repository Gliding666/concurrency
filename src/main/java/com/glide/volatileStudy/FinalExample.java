package com.glide.volatileStudy;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test")
public class FinalExample {
    int i; // 普通变量
    final int j; // final 变量
    static FinalExample obj;

    public FinalExample() { // 构造函数
        i = 1; // 写普通域
        j = 2; // 写 final 域
    }

    public static void writer() { // 写线程 A 执行
        obj = new FinalExample();
    }

    public static void reader() { // 读线程 B 执行
        FinalExample object = obj; // 读对象引用
        int a = object.i; // 读普通域         a=1或者a=0或者直接报错i没有初始化
        int b = object.j; // 读 final域      b=2
        if(a!=1) log.info(a + "  " + b);
    }
}

class Test{
    public static void main(String[] args) {
        for(int i = 0; i < 1;i++) {
//            new Thread(()->{
//                FinalExample.reader();
//            }).start();
            new Thread(()->{
                FinalExample.writer();
            }).start();

        }



    }
}