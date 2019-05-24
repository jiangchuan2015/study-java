package chuan.study.algorithm.hello;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-23
 */
public class Search {

    public static int binSearch(int[] a, int val) {
        int low = 0, high = a.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            int v = a[mid];
            if (v == val) {
                return mid;
            } else if (v < val) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int maxCount = 100;
        int[] a = new int[maxCount];
        IntStream.iterate(0, i -> i + 1).limit(maxCount).forEach(idx -> {
            a[idx] = ThreadLocalRandom.current().nextInt(10000);
        });

        Arrays.sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println(binSearch(a, a[ThreadLocalRandom.current().nextInt(maxCount)]));

    }
}
