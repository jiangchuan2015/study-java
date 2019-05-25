package chuan.study.concurrent.hello;

import java.util.concurrent.CountDownLatch;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-24
 */
public class CountDownLatchTest {
    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        CountDownLatch startSignal = new CountDownLatch(1);

        for (int i = 1; i <= N; i++) {
            new Thread(new Worker(i, doneSignal, startSignal)).start();
        }
        System.out.println("begin------------");
        startSignal.countDown();//开始执行啦
        doneSignal.await();//等待所有的线程执行完毕
        System.out.println("Ok");

    }

    static class Worker implements Runnable {
        private final CountDownLatch doneSignal;
        private final CountDownLatch startSignal;
        private int beginIndex;

        Worker(int beginIndex, CountDownLatch doneSignal,
               CountDownLatch startSignal) {
            this.startSignal = startSignal;
            this.beginIndex = beginIndex;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                beginIndex = (beginIndex - 1) * 10 + 1;
                for (int i = beginIndex; i <= beginIndex + 10; i++) {
                    System.out.println(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                doneSignal.countDown();
            }
        }
    }
}
