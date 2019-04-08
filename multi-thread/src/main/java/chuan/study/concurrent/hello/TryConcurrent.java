package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-08
 */
@Slf4j
public class TryConcurrent {
    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                doSomething("听音乐");
            }
        };
        t1.setName("听音乐");


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                doSomething("看新闻");
            }
        }, "看新闻");


        Thread t3 = new Thread(() -> doSomething("睡懒觉"), "睡懒觉");

        t1.start();
        t2.start();
        t3.start();

        doSomething("做运动");
    }

    private static void doSomething(String something) {
        IntStream.iterate(0, i -> i + 1).forEach(idx -> {
            log.error("我正在{}...", something);
            TryConcurrent.sleep(1);
        });
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
