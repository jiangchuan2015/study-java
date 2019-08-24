package chuan.study.concurrent.gof.singleton;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-15
 */
public final class Singleton3 {
    private static volatile Singleton3 instance = null;

    /**
     * 私有构造函数，不允许外部 new
     */
    private Singleton3() {}

    public static Singleton3 getInstance() {
        if (null == instance) {
            synchronized (Singleton3.class) {
                if (null == instance) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }
}
