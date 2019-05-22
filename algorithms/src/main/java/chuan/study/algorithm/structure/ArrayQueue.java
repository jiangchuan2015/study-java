package chuan.study.algorithm.structure;

import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-22
 */
public class ArrayQueue<T> {
    private int capacity;
    private int head;
    private int tail;

    private T[] array;

    public ArrayQueue(int capacity) {
        this.array = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.head = 0;
        this.tail = 0;
    }

    public boolean enqueue(T val) {
        if (this.tail >= this.capacity) {
            this.tail = 0;
        }

        this.array[this.tail++] = val;
        return true;
    }

    public T dequeue() {
        if (this.head >= this.capacity) {
            this.head = 0;
        }

        T val = this.array[this.head];
        this.array[this.head] = null;
        this.head++;
        return val;
    }

    public static void main(String[] args) {
        ArrayQueue<String> queue = new ArrayQueue<>(5);

        IntStream.iterate(65, i -> i + 1).limit(6).forEach(idx -> {
            char c = (char) idx;
            System.out.println(String.format("Put '%s': %s", c, queue.enqueue(String.valueOf(c))));
        });

        IntStream.iterate(1, i -> i + 1).limit(10).forEach(idx -> {
            System.out.println(String.format("Pop %s, the result is %s", idx, queue.dequeue()));
        });
    }
}
