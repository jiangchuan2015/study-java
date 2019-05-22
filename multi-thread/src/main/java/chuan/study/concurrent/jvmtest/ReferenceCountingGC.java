package chuan.study.concurrent.jvmtest;

/**
 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-02-10
 */
public class ReferenceCountingGC {
    public Object instance = null;
    private byte[] bigSize = new byte[2 * 1024 * 1024];

    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        System.gc();
    }

}
