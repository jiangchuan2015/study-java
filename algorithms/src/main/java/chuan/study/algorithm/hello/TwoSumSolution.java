package chuan.study.algorithm.hello;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     *
     * @param nums   输入的数
     * @param target 目标数
     * @return 下票数组
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexMap = new HashMap<>((int) Math.ceil(nums.length / 0.75));
        for (int i = 0; i < nums.length; i++) {
            int remain = target - nums[i];
            if (indexMap.containsKey(remain)) {
                return new int[]{indexMap.get(remain), i};
            }
            indexMap.put(nums[i], i);
        }
        return new int[2];
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[] results = TwoSumSolution.twoSum(new int[]{2, 7, 11, 15}, 9);
        long end = System.currentTimeMillis() - start;

        System.out.println(end + ":" + Arrays.toString(results));
    }
}
