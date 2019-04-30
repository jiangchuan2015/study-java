package chuan.study.concurrent.hello;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-30
 */
public class ReadWriteLockSample {
    private final Map<String, String> map = new TreeMap<>();

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    public String get(String key) {
        readLock.lock();
        System.out.println("获取到读锁");
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void put(String key, String val) {
        writeLock.lock();
        System.out.println("获取到写锁");
        try {
            map.put(key, val);
        } finally {
            writeLock.unlock();
        }
    }
}
