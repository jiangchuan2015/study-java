package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-24
 */
@Slf4j
public class CountDownLatchTest {
    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        CountDownLatch startSignal = new CountDownLatch(1);

        IntStream.rangeClosed(1, N).forEach(idx -> new Thread(new Worker(idx, doneSignal, startSignal)).start());

        // 开始执行啦
        log.info("执行 ==> 开始");
        startSignal.countDown();

        // 等待所有的线程执行完毕
        doneSignal.await();
        log.info("执行 ==> 结束");
    }

    static class Worker implements Runnable {
        private final CountDownLatch doneSignal;
        private final CountDownLatch startSignal;
        private int beginIndex;

        Worker(int beginIndex, CountDownLatch doneSignal, CountDownLatch startSignal) {
            this.startSignal = startSignal;
            this.beginIndex = beginIndex;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                beginIndex = (beginIndex - 1) * 10 + 1;
                IntStream.range(beginIndex, beginIndex + 10).forEach(idx -> log.debug("{}", idx));
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                doneSignal.countDown();
            }
        }
    }
}
