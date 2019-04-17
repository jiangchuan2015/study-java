package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-04-09
 */
@Slf4j
public class BooleanLock {
    private boolean locked;
    private Thread currentThread;
    private Set<Thread> blockedThreads = new HashSet<>();

    public void lock() throws InterruptedException {
        // lock方法体使用synchronized包裹，lock完就退出了锁
        synchronized (this) {
            // 如果locked标志为true，表示锁被别人获取了
            while (locked) {
                blockedThreads.add(Thread.currentThread());
                try {
                    // locked标志为true，当前线程就等待locked为false，自身等待，wait方法释放真实的锁
                    this.wait();
                } catch (InterruptedException ex) {
                    // 这里线程是上面的wait方法被打断会跳出，因为线程是调用wait方法主动阻塞
                    // 而不是在等待synchronized锁释放而陷入阻塞，所以是可以打断的
                    // 所以这里是整个BooleanLock机制的核心!!!
                    blockedThreads.remove(Thread.currentThread());
                    throw new InterruptedException(Thread.currentThread().getName() + "被打断");
                }
            }

            // 如果走到这里，表示之前没有线程获取到锁，我获取到了
            blockedThreads.remove(Thread.currentThread());

            // 上锁
            this.locked = true;
            this.currentThread = Thread.currentThread();
        }
    }

    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this) {
            if (mills <= 0) {
                this.lock();
            } else {
                long lastMills = mills;
                // 这里记录一个时间戳，totalMills表示到达这个时刻，如果还没有获取到锁，就报超时
                long timeoutMills = System.currentTimeMillis() + mills;
                while (locked) {
                    if (lastMills <= 0) {
                        throw new TimeoutException(Thread.currentThread().getName() + "超时" + mills + "ms" + "没有获取到锁");
                    }

                    blockedThreads.add(Thread.currentThread());

                    // 这里释放了真实的锁
                    this.wait(lastMills);

                    // lastMills表示还需要等待多久时间，而lastMills减少表示两种情况
                    // 第一种: 线程已经wait到时间了，自动唤醒了，这个时候已经过去了lastMills的时间，下面这个操作就把lastMills置0
                    // 第二种: 线程wait没有到时间，被unlock唤醒，但是被唤醒后还是没有抢到锁，于是lastMills被减少，
                    //        继续等待，时间减去已经等待了多长时间
                    lastMills = timeoutMills - System.currentTimeMillis();
                }

                // 如果走到这里，表示之前没有线程获取到锁，我获取到了
                blockedThreads.remove(Thread.currentThread());

                // 上锁
                locked = true;
                currentThread = Thread.currentThread();
            }
        }
    }

    public void unlock() {
        synchronized (this) {
            // 如果当前线程是获取到锁的线程，就可以去执行释放锁的操作
            if (Objects.equals(Thread.currentThread(), this.currentThread)) {
                // 释放锁
                this.locked = false;

                // 唤醒所有正在wait而陷入阻塞的线程，因为locked已经被设置成false，所以可以进入下一轮争抢
                this.notifyAll();
            }
        }
    }


    public List<Thread> getBlockedThreads() {
        return Collections.unmodifiableList(new ArrayList<>(blockedThreads));
    }

    public static void main(String[] args) {
        BooleanLock booleanLock = new BooleanLock();
        IntStream.rangeClosed(1, 10).forEach(idx -> {
            new Thread(() -> {
                try {
                    booleanLock.lock(TimeUnit.SECONDS.toMillis(5));
                    long sleepTime = ThreadLocalRandom.current().nextInt(100);
                    log.error("{} 得到了锁, 暂停 {}秒", Thread.currentThread(), sleepTime);
                    log.error("{}", StringUtils.join(booleanLock.getBlockedThreads(), ", "));
                    TimeUnit.SECONDS.sleep(sleepTime);
                } catch (InterruptedException | TimeoutException ex) {
                    log.error(ex.getMessage(), ex);
                } finally {
                    booleanLock.unlock();
                }
            }, "锁-" + idx).start();
        });
    }
}
