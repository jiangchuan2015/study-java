package chuan.study.concurrent.gof.singleton;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-15
 */
public final class Singleton {
    /**
     * 饿汉式, 系统启动时就初始化类
     */
    private static Singleton instance = new Singleton();

    /**
     * 私有构造函数，不允许外部 new
     */
    private Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }
}
