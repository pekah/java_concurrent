package com.eli.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemapDemo implements Runnable {
    // 每次允许5个线程同时进入
    final Semaphore semaphore = new Semaphore(5);

    @Override
    public void run() {
        try {
            // 申请准入许可。如果申请不到当前线程会被阻塞，直到申请成功或者被中断。申请是个耗时的操作，可以被中断，中断时会抛出InterruptedException
            semaphore.acquire();
            // 模拟耗时操作
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + ":done!");
            // 释放准入许可
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        final SemapDemo demo = new SemapDemo();

//        for (int i = 0; i < 20; i++) {
//            executorService.submit(demo);
//        }

        final SemapDemo demo = new SemapDemo();
        for (int i = 0; i < 20; i++) {
            new Thread(demo).start();
        }
    }


}
