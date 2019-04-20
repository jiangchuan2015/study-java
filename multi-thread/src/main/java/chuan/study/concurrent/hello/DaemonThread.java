package chuan.study.concurrent.hello;

import java.util.concurrent.TimeUnit;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-20
 */
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    System.out.println(Thread.currentThread().getName() + ": I'm a dead loop thread.");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 如果线程不设置成 守护线程 程序将永远不能结束
        // thread.setDaemon(true);
        thread.start();

        TimeUnit.SECONDS.sleep(2);
        System.out.println("Main thread finished lifecycle.");
    }
}
