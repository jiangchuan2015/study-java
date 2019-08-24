package chuan.study.concurrent.gof.singleton;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-15
 */
public enum Singleton5 {
    /**
     * 单例
     */
    INSTANCE;

    /**
     * 私有构造函数，不允许外部 new
     */
    Singleton5() {}

    public static Singleton5 getInstance() {
        return INSTANCE;
    }
}
