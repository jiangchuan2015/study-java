package chuan.study.jvm;

/**
 * 大对象直接进入老年代
 *
 * JVM参数
 * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10m -XX:MaxTenuringThreshold=15 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:survivor_live.log
 *
 * https://zhuanlan.zhihu.com/p/366772272
 * @author Jiang Chuan
 * @date 2021-06-11
 */
public class BigObjectTest {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] array1 = new byte[2 * _1MB];
        array1 = new byte[2 * _1MB];
        array1 = new byte[2 * _1MB];

        byte[] array2 = new byte[128 * 1024];
        array2 = null;

        // 这里触发第一次minor gc
        byte[] array3 = new byte[2 * _1MB];

        System.out.println("end");
    }
}
