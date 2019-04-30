package chuan.study.concurrent.hello;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-30
 */
public class ThreadSafeSample {
    private int sharedState;

    public void nonSafeAction() {
        int former = 0, latter = 0;
        while (sharedState < 100_000) {
            // 保证可见性
            /*synchronized (this)*/ {
                former = sharedState++;
                latter = sharedState;
            }

            if (former != (latter - 1)) {
                System.out.println(String.format("Observed data race, former is %s, later is %s", former, latter));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeSample sample = new ThreadSafeSample();

        Thread t1 = new Thread(sample::nonSafeAction);
        Thread t2 = new Thread(sample::nonSafeAction);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
