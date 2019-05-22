package chuan.study.algorithm;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-22
 */
public class Test {

    public static String swapChar(String input) {
        if (input == null) {
            return null;
        }
        char[] original = input.toCharArray();
        char[] changed = new char[original.length];
        for (int i = 0; i < original.length / 2; i++) {
            int start = i * 2;
            int next = i * 2 + 1;
            changed[start] = original[next];
            changed[next] = original[start];
        }
        if (original.length % 2 == 1) {
            changed[original.length - 1] = original[original.length - 1];
        }

        return new String(changed);
    }

    public static void main(String[] args) {
        System.out.println(swapChar("12345678"));
    }
}
