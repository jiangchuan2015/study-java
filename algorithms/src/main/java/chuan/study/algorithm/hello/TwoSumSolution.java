package chuan.study.algorithm.hello;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * https://leetcode.com/problems/two-sum/
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-22
 */
public class TwoSumSolution {
    /**
     * 私有构造函数
     */
    private TwoSumSolution() {
    }

    /**
     * 求满足条件的下标
     * 时间复杂度:  o(n^2)
     *
     * @param nums   输入的数
     * @param target 目标数
     * @return 下标数组
     */
    public static int[] twoSum1(int[] nums, int target) {
        if (null == nums || nums.length < 2) {
            return new int[0];
        }

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[0];
    }

    /**
     * 求满足条件的下标
     * 时间复杂度:  o(n)
     *
     * @param nums   输入的数
     * @param target 目标数
     * @return 下标数组
     */
    public static int[] twoSum(int[] nums, int target) {
        if (null == nums || nums.length < 2) {
            return new int[0];
        }

        Map<Integer, Integer> indexMap = new HashMap<>((int) Math.ceil(nums.length / 0.75));
        for (int i = 0; i < nums.length; i++) {
            Integer index = indexMap.get(target - nums[i]);
            if (Objects.nonNull(index)) {
                return new int[]{i, index};
            }
            indexMap.put(nums[i], i);
        }

        return new int[0];
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[] results = TwoSumSolution.twoSum(new int[]{2, 7, 11, 15}, 9);
        long end = System.currentTimeMillis() - start;

        System.out.println(end + ":" + Arrays.toString(results));
    }
}
