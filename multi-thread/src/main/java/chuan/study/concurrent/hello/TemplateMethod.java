package chuan.study.concurrent.hello;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-08
 */
public class TemplateMethod {
    public static void main(String[] args) {
        new BaseInner() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("## " + message + "  ##");
            }
        }.print("Hello world");

        // 我是分隔符号
        System.out.println();

        new BaseInner() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("** " + message + "  **");
            }
        }.print("Hello world");
    }


    static abstract class BaseInner {
        abstract void wrapPrint(String message);

        final void print(String message) {
            System.out.println("######################");
            wrapPrint(message);
            System.out.println("######################");
        }
    }
}
