package chuan.study.algorithm.hello;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-22
 */
public class ValidParenthesesSolution {
    private static final Map<Character, Character> charMap = new HashMap<Character, Character>() {{
        put(')', '(');
        put(']', '[');
        put('}', '{');
    }};


    /**
     * 1. 左 => push
     * 2. 右 => peek & pop
     * 3. 检查栈是否为空
     *
     * @param str
     * @return
     */
    public static boolean isValid(String str) {
        if (null == str || str.length() == 0 || str.length() % 2 != 0) {
            return false;
        }

        Stack<Character> stack = new Stack<>();
        for (char c : str.toCharArray()) {
            if (charMap.containsValue(c)) {
                stack.push(c);
            } else if (stack.isEmpty() || !Objects.equals(stack.pop(), charMap.get(c))) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println(isValid("()"));
        System.out.println(isValid("(]"));
        System.out.println(isValid("((([])))"));
        System.out.println(isValid("((([{}]))))"));
    }
}
