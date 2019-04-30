package chuan.study.concurrent.hello;

import java.util.concurrent.TimeUnit;

/**
 * 1. 查出进程号
 * ${JAVA_HOME}/bin/jps -ml
 *
 * 2. 查看堆栈信息
 * ${JAVA_HOME}/bin/stack PID
 *
 * 3. 从输出的信息中可看到一个死锁
 * 2019-04-30 10:36:35
 * Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.211-b12 mixed mode):
 *
 * "线程二" #13 prio=5 os_prio=0 tid=0x000000001ef3c000 nid=0x7d14 waiting for monitor entry [0x000000001fcce000]
 *    java.lang.Thread.State: BLOCKED (on object monitor)
 * 	at chuan.study.concurrent.hello.DeadLockSample.run(DeadLockSample.java:27)
 * 	- waiting to lock <0x000000076cb2f520> (a java.lang.String)
 * 	- locked <0x000000076cb2f558> (a java.lang.String)
 *
 * "线程一" #12 prio=5 os_prio=0 tid=0x000000001ef3b800 nid=0x6f80 waiting for monitor entry [0x000000001fbcf000]
 *    java.lang.Thread.State: BLOCKED (on object monitor)
 * 	at chuan.study.concurrent.hello.DeadLockSample.run(DeadLockSample.java:27)
 * 	- waiting to lock <0x000000076cb2f558> (a java.lang.String)
 * 	- locked <0x000000076cb2f520> (a java.lang.String)
 *
 * ......
 *
 * Found one Java-level deadlock:
 * =============================
 * "线程二":
 *   waiting to lock monitor 0x000000001cd83408 (object 0x000000076cb2f520, a java.lang.String),
 *   which is held by "线程一"
 * "线程一":
 *   waiting to lock monitor 0x000000001ef3df38 (object 0x000000076cb2f558, a java.lang.String),
 *   which is held by "线程二"
 *
 * Java stack information for the threads listed above:
 * ===================================================
 * "线程二":
 * 	at chuan.study.concurrent.hello.DeadLockSample.run(DeadLockSample.java:27)
 * 	- waiting to lock <0x000000076cb2f520> (a java.lang.String)
 * 	- locked <0x000000076cb2f558> (a java.lang.String)
 * "线程一":
 * 	at chuan.study.concurrent.hello.DeadLockSample.run(DeadLockSample.java:27)
 * 	- waiting to lock <0x000000076cb2f558> (a java.lang.String)
 * 	- locked <0x000000076cb2f520> (a java.lang.String)
 *
 * Found 1 deadlock.
 *
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-30
 */
public class DeadLockSample extends Thread {
    private String first, second;

    public DeadLockSample(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + " obtained: " + first);

            try {
                TimeUnit.SECONDS.sleep(1);
                synchronized (second) {
                    System.out.println(this.getName() + " obtained: " + first);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String first = "LOCK-A", second = "LOCK-B";
        DeadLockSample t1 = new DeadLockSample("线程一", first, second);
        DeadLockSample t2 = new DeadLockSample("线程二", second, first);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
