package chuan.study.algorithm.hello;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-23
 */
public class Sort {
    public static void bubbleSort(int[] a) {
        if (a.length <= 1) {
            return;
        }

        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (a[i] > a[j]) {
                    int tmp = a[j];
                    a[j] = a[i];
                    a[i] = tmp;
                }
            }
        }
    }

    public static void insertionSort(int[] a) {
        if (a.length <= 1) {
            return;
        }

        int n = a.length;
        for (int i = 1; i < n; ++i) {
            int val = a[i];
            int j = i - 1;
            // 查找插入的位置
            for (; j >= 0; --j) {
                if (a[j] > val) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = val;
        }
    }

    public static void selectionSort(int[] a) {
        if (a.length <= 1) {
            return;
        }

        for (int i = 0; i < a.length - 1; i++) {
            int k = i;
            for (int j = k + 1; j < a.length; j++) {
                if (a[j] < a[k]) {
                    k = j;
                }
            }

            if (i != k) {
                int temp = a[i];
                a[i] = a[k];
                a[k] = temp;
            }
        }

    }

    public static void main(String[] args) {
        int maxCount = 100;
        int[] srcArr = new int[maxCount];
        IntStream.iterate(0, i -> i + 1).limit(maxCount).forEach(idx -> {
            srcArr[idx] = ThreadLocalRandom.current().nextInt(10000);
        });


        int[] arr1 = new int[maxCount];
        System.arraycopy(srcArr, 0, arr1, 0, maxCount - 1);
        long start = System.nanoTime();
        bubbleSort(arr1);
        System.out.println("bubbleSort: " + (System.nanoTime() - start));

        int[] arr2 = new int[maxCount];
        System.arraycopy(srcArr, 0, arr1, 0, maxCount - 1);
        start = System.nanoTime();
        insertionSort(arr2);
        System.out.println("insertionSort: " + (System.nanoTime() - start));

        int[] arr3 = new int[maxCount];
        System.arraycopy(srcArr, 0, arr1, 0, maxCount - 1);
        start = System.nanoTime();
        selectionSort(arr3);
        System.out.println("selectionSort: " + (System.nanoTime() - start));
    }
}
