package com.eli.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo implements Runnable {
    static final CountDownLatch end = new CountDownLatch(10);

    public void run() {
        try {
            // 模拟检查任务
            Thread.sleep(new Random().nextInt(10)*1000);
            System.out.println("check complete！");
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException{
        ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            exec.submit(new CountDownLatchDemo());
        }

        // 等待检查
        end.await();
        // 发射火箭
        System.out.println("Fire!");
        exec.shutdown();
    }
}
