package com.eli.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhouyilin on 2018/5/1.
 */
public class Counter {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int i = 0;

    public static void main(String[] args) {
        final Counter counter = new Counter();
        List<Thread> threads = new ArrayList<Thread>(600);
        long start = System.currentTimeMillis();

        for (int j = 0; j < 100; j++){
            Thread thread = new Thread( () -> {
                for (int i = 0; i < 10000; i++){
                    counter.safeCount();
                    counter.count();
                }
            });
            threads.add(thread);
        }

        threads.forEach(t -> t.start());

        // 等待所有线程执行完成
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException exception){
                exception.printStackTrace();
            }
        });

        System.out.println(counter.i);
        System.out.println(counter.atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    // 使用CAS实现线程安全计数器
    private void safeCount(){
        for (;;){
            int i = atomicInteger.get();
            boolean suc = atomicInteger.compareAndSet(i, ++i);
            if(suc){
                break;
            }
        }

    }

    // 非线程安全计数器
    private void count(){
        i++;
    }
}
