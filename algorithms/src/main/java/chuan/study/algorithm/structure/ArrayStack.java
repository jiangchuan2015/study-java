package chuan.study.algorithm.structure;

import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-22
 */
public class ArrayStack<T> {
    private int capacity;
    private int pointer;

    private T[] array;

    public ArrayStack(int capacity) {
        this.array = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.pointer = 0;
    }

    public T pop() {
        if (this.pointer == 0) {
            return null;
        }
        this.pointer--;
        return this.array[this.pointer];
    }

    public boolean push(T val) {
        if (this.pointer == this.capacity) {
            this.resize();
        }

        this.array[pointer++] = val;
        return true;
    }

    private void resize() {
        int newCapacity = this.capacity + (int) Math.ceil(this.capacity / 2);
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(this.array, 0, newArray, 0, capacity);

        this.array = newArray;
        this.capacity = newCapacity;
    }

    public static void main(String[] args) {
        final int maxCount = 10;
        final ArrayStack<String> stack = new ArrayStack<>(5);

        IntStream.iterate(65, i -> i + 1).limit(maxCount).forEach(idx -> {
            char c = (char) idx;
            System.out.println(String.format("Put '%s': %s", c, stack.push(String.valueOf(c))));
        });


        IntStream.iterate(1, i -> i + 1).limit(maxCount).forEach(idx -> {
            System.out.println(String.format("Pop %s, the result is %s", idx, stack.pop()));
        });
    }
}
