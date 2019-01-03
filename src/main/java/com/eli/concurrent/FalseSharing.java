package com.eli.concurrent;


import sun.misc.Contended;

/**
 * CPU Cache优化，解决伪共享问题
 * CPU有一个高速缓存Cache，读写数据的最小单位为缓存行（Cache Line）。
 * 它是从主存（Memory）复制到缓存（Cache）的最小单位，一般为32字节到128字节。
 * 两个变量放在同一个缓存行时，在多线程环境下，可能会相互影响彼此的性能。
 * 比如A变量被更新了，会导致缓存行失效，从而导致同在缓存行的B变量也变成无效，Cache无法命中。
 * 解决办法：在A变量的前后空间都先占据一定的位置，即padding。防止A、B变量都同在一个缓存行上
 *
 * 此demo中，是为了防止第一个VolatileLong.value和第二个VolatileLong.value进入同一个缓存行
 *
 * 不解决伪共享：duration=17814
 * 使用填充解决伪共享：duration=2972
 * 使用@Contended注解解决伪共享，需要指定虚拟机参数-XX:-RestrictContended  ：duration=2933
 *
 */
public class FalseSharing implements Runnable{
    public final static int NUM_THREADS = 2;
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) throws Exception{
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration=" + (System.currentTimeMillis() - start));
    }


    private static void runTest() throws InterruptedException{
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    @Contended
    public final static class VolatileLong {
        public volatile long value = 0L;
//        public long p1,p2,p3,p4,p5,p6,p7; // comment out
    }
}
