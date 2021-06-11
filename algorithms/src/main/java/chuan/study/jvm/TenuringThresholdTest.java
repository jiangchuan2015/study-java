package chuan.study.jvm;

/**
 * 存活对象达到年龄阈值后直接进入老年代
 *
 * JVM参数
 * -XX:NewSize=15m -XX:MaxNewSize=15m -XX:InitialHeapSize=50m -XX:MaxHeapSize=50m -XX:SurvivorRatio=3 -XX:PretenureSizeThreshold=10m -XX:MaxTenuringThreshold=2 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:survivor_live.log
 * <p>
 * https://zhuanlan.zhihu.com/p/366898729
 *
 * @author Jiang Chuan
 * @date 2021-06-11
 */
public class TenuringThresholdTest {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] array1 = new byte[2 * _1MB];
        array1 = new byte[2 * _1MB];
        array1 = new byte[2 * _1MB];
        array1 = new byte[2 * _1MB];
        array1 = null;

        // 经过三次GC后，想让这个128K进入老年代
        byte[] array2 = new byte[128 * 1024];

        // 第一次minor gc，此时array2指向的对象0岁
        byte[] array3 = new byte[2 * _1MB];
        array3 = new byte[2 * _1MB];
        array3 = new byte[2 * _1MB];
        array3 = new byte[2 * _1MB];
        array3 = null;

        // 第二次minor gc，此时array2指向的对象1岁
        byte[] array4 = new byte[2 * _1MB];
        array4 = new byte[2 * _1MB];
        array4 = new byte[2 * _1MB];
        array4 = new byte[2 * _1MB];
        array4 = null;

        // 第三次minor gc，此时array2指向的对象2岁
        byte[] array5 = new byte[2 * _1MB];

    }
}
