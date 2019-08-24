package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 多线程有上下文切换问题，计算时会花更多时间
 *
 * @author jiang.chuan
 * @version 1.0.0
 * @date 2019-08-24
 */
@Slf4j
public class ContextSwitchTest {
    public static void main(String[] args) {
        MultiThreadTest mt = new MultiThreadTest();
        mt.start();

        SerialTest st = new SerialTest();
        st.start();
    }


    static class MultiThreadTest extends BaseContextSwitchTest {
        @Override
        public void start() {
            int threadCount = 5;
            long start = System.currentTimeMillis();
            Thread[] threads = new Thread[threadCount];

            // 启动多线程
            IntStream.range(0, threadCount).forEach(idx -> {
                threads[idx] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (counter < count) {
                            synchronized (this) {
                                if (counter < count) {
                                    increase();
                                }
                            }
                        }
                    }
                });

                threads[idx].start();
            });

            // 收集结果
            Arrays.stream(threads).forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage(), ex);
                }
            });

            long end = System.currentTimeMillis();
            log.info("计算结果: {}, 多线程执行时间: {}ms", counter, (end - start));
        }
    }

    static class SerialTest extends BaseContextSwitchTest {
        @Override
        public void start() {
            long start = System.currentTimeMillis();
            IntStream.range(0, count).forEach(idx -> increase());
            long end = System.currentTimeMillis();
            log.info("计算结果: {}, 串行执行时间: {}ms", counter, (end - start));
        }
    }


    static abstract class BaseContextSwitchTest {
        static final int count = 100_000_000;
        volatile int counter = 0;

        void increase() {
            this.counter += 1;
        }

        public abstract void start();
    }
}
