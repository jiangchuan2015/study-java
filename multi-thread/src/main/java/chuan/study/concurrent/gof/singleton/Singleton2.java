package chuan.study.concurrent.gof.singleton;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-15
 */
public final class Singleton2 {
    /**
     * 饿汉式, 系统启动时就初始化类
     */
    private static Singleton2 instance = null;

    /**
     * 私有构造函数，不允许外部 new
     */
    private Singleton2() {
    }

    public static synchronized Singleton2 getInstance() {
        if(null == instance) {
            instance = new Singleton2();
        }
        return instance;
    }
}
