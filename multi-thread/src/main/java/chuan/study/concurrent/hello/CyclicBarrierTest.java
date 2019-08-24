package chuan.study.concurrent.hello;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-24
 */
@Slf4j
public class CyclicBarrierTest implements Runnable {
    private final CyclicBarrier barrier;
    private final String name;

    public CyclicBarrierTest(CyclicBarrier barrier, String name) {
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000));
            log.info("{} ready", name);
            barrier.await();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        log.info("{} go", name);
    }


    public static void main(String[] args) {
        final int maxThread = 10;
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService threadPool = new ThreadPoolExecutor(3,
                maxThread, 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1024),
                new ThreadFactoryBuilder().setNameFormat("BARRIER-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        IntStream.iterate(1, i -> i + 2).limit(maxThread).forEach(idx -> {
            threadPool.execute(new CyclicBarrierTest(barrier, String.valueOf(idx)));
        });

        threadPool.shutdown();
    }
}
