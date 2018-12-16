package com.eli.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static class ThreadTest implements Runnable {

        private final CyclicBarrier cyclicBarrier;
        private final int N;

        ThreadTest(CyclicBarrier cyclicBarrier, int N) {
            this.cyclicBarrier = cyclicBarrier;
            this.N = N;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }

    public static class CyclicBarrierCallback implements Runnable {
        private int N;

        CyclicBarrierCallback(int N) {
            this.N = N;
        }

        @Override
        public void run() {
            System.out.println("第" + N + "批10个线程执行完毕");
            N=N+1;
        }
    }

    public static void main(String[] args) throws Exception{
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new CyclicBarrierCallback(1));

        for (int i = 0; i < 100; i++) {
            if(i%10==0) {
                Thread.sleep(1000);
            }

            new Thread(new ThreadTest(cyclicBarrier, i)).start();
        }
    }
}
