package com.gxh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class StudyCompletableFuture2 {

    static List<Goods> list = Arrays.asList(
            new Goods("jd"),
            new Goods("taobao"),
            new Goods("pdd")
    );

    public static List<String> getPrice(List<Goods> list, String findName) {
        return list
                .stream()
                .map(goods -> String.format(findName + " in %s price is %.2f 元",
                        goods.getGoodsName(), goods.getPrice()))
                .collect(Collectors.toList());
    }

    public static List<String> getPriceWithCompletableFuture(List<Goods> list, String findName) {
        return list.stream().map(goods ->
                CompletableFuture.supplyAsync(()-> String.format(findName + " in %s price is %.2f 元",
                goods.getGoodsName(),
                goods.getPrice())))
                .collect(Collectors.toList())
                .stream()
                .map(s -> s.join())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

//        long start = System.currentTimeMillis();
//        List<String> list1 = getPrice(list, "mysql");
//        for(String e : list1){
//            System.out.println(e);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("花费时间：" + ( end - start));
        System.out.println("------------------------------------------");
        long start1 = System.currentTimeMillis();
        List<String> list2 = getPriceWithCompletableFuture(list, "mysql");
        for(String e : list2){
            System.out.println(e);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("花费时间：" + ( end1 - start1));


    }
}

class Goods {
    private String goodsName;
    public Goods (String name){
        this.goodsName = name;
    }
    public String getGoodsName(){
        return this.goodsName;
    }
    public double getPrice()  {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble(1) * 100;
    }
}
