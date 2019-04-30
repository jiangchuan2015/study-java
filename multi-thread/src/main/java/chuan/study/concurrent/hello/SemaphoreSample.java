package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-30
 */
public class SemaphoreSample {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        IntStream.range(0, 10).forEach(idx -> {
            new Thread(new SemaphoreWorker(semaphore), "线程 - " + idx).start();
        });
    }
}

@Slf4j
class SemaphoreWorker implements Runnable {
    private Semaphore semaphore;

    public SemaphoreWorker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            log.info("is waiting for a permit!");
            semaphore.acquire();
            log.info("executed!");
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            log.info("released a permit!");
            semaphore.release();
        }
    }
}