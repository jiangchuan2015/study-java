package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-25
 */
@Slf4j
public class UncaughtExceptionHandlerTest {
    public static void main(String[] args) {
        setDefaultUncaughtExceptionHandler();
        test();
    }

    private static void test() {
        new Thread(() -> {
            log.error("将了现子线程异常: {}", 1 / 0);
        }).start();

        log.error("当前线程异常: {}", 1 / 0);
        log.error("后面的代码将不会执行");
    }

    private static void setDefaultUncaughtExceptionHandler() {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> log.error("线程:{}，出现了异常: {}", t, e.getMessage(), e));
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("全局异常信息", e);
            }
        });
    }
}
