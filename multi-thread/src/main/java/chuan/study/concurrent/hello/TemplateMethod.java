package chuan.study.concurrent.hello;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-08
 */
public class TemplateMethod {
    public static void main(String[] args) {
        new AbstractInnerPrinter() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("## " + message + "  ##");
            }
        }.print("Hello world");

        // 我是分隔符号
        System.out.println();

        new AbstractInnerPrinter() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("** " + message + "  **");
            }
        }.print("Hello world");
    }


    static abstract class AbstractInnerPrinter {
        /**
         * 打印消息
         *
         * @param message 消息内容
         */
        abstract void wrapPrint(String message);

        /**
         * 模板模式输出消息
         *
         * @param message 消息内容
         */
        final void print(String message) {
            System.out.println("######################");
            wrapPrint(message);
            System.out.println("######################");
        }
    }
}
