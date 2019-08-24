package chuan.study.concurrent.hello;

import java.util.HashSet;
import java.util.Set;

/**
 * 拿转账来说，引入账户管理员，一次申请两个资源（转出账户和转入账户），只有两个账户可以同时使用时才申请成功
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-10
 */
public class Allocator {
    /**
     * 需要申请的资源
     */
    private static final Set<Object> RESOURCES = new HashSet<>();

    /**
     * 私有化构造方法
     */
    private Allocator() {
    }

    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static Allocator instance = new Allocator();
    }

    public static Allocator getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 一次性申请所有资源
     */
    public synchronized boolean apply(Object from, Object to) {
        if (RESOURCES.contains(from) || RESOURCES.contains(to)) {
            return false;
        } else {
            RESOURCES.add(from);
            RESOURCES.add(to);
        }
        return true;
    }

    /**
     * 归还资源
     */
    synchronized void free(Object from, Object to) {
        RESOURCES.remove(from);
        RESOURCES.remove(to);
    }
}

class Account {
    private Allocator allocator = Allocator.getInstance();
    private int balance;

    /**
     * 转账
     */
    public void transfer(Account target, int amt) {
        // 一次性申请转出账户和转入账户，直到成功
        while (!allocator.apply(this, target)) {
            try {
                // 锁定转出账户
                synchronized (this) {
                    // 锁定转入账户
                    synchronized (target) {
                        if (this.balance > amt) {
                            this.balance -= amt;
                            target.balance += amt;
                        }
                    }
                }
            } finally {
                allocator.free(this, target);
            }
        }
    }
}
