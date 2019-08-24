package chuan.study.concurrent.gof.singleton;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-06-15
 */
public final class Singleton4 {
    /**
     * 私有构造函数，不允许外部 new
     */
    private Singleton4() {}

    /**
     * 运用JVM类加载的特点，在主动引用时初始化
     */
    private static class Holder {
        private static Singleton4 instance = new Singleton4();
    }

    public static Singleton4 getInstance() {
        return Holder.instance;
    }
}
