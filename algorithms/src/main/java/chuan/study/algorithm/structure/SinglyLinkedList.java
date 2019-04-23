package chuan.study.algorithm.structure;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * 链表
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-23
 */
public class SinglyLinkedList<T> {
    private Node<T> head = null;

    /**
     * 根据指定的值查询节点
     *
     * @param value 链上的值
     * @return 查询到的节点
     */
    public Node<T> findByValue(T value) {
        if (null == head) {
            insertToHead(value);
            return head;
        }

        Node<T> current = head;
        while (null != current && !Objects.equals(current.getItem(), value)) {
            current = current.getNext();
        }

        return current;
    }

    /**
     * 根据索引位置查询节点
     *
     * @param index 链上的索引
     * @return 查询到的节点
     */
    public Node<T> findByIndex(int index) {
        if (null == head) {
            return null;
        }

        int position = 0;
        Node<T> current = head;
        while (null != current && position != index) {
            current = current.getNext();
            position++;
        }
        return current;
    }

    /**
     * 将数据插入到链表头部
     *
     * @param value 要插入的值
     */
    public void insertToHead(T value) {
        Node<T> newNode = new Node<>(value);
        if (null == head) {
            this.head = newNode;
        } else {
            newNode.setNext(this.head);
            this.head = newNode;
        }
    }

    /**
     * 将数据插入到链表尾部
     *
     * @param value 要插入的值
     */
    public void insertTail(T value) {
        Node<T> newNode = new Node<>(value);
        if (null == head) {
            this.head = newNode;
        } else {
            Node<T> current = head;
            while (null != current.getNext()) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    /**
     * 在链表指定节点后插入数据
     *
     * @param node  链表的指定的节点
     * @param value 要插入的值
     */
    public void insertAfter(Node<T> node, T value) {
        if (null == node) {
            return;
        }

        Node<T> newNode = new Node<>(value);
        if (null != node.getNext()) {
            newNode.setNext(node.getNext());
        }
        node.setNext(newNode);
    }

    /**
     * 在链表指定节点前插入数据
     *
     * @param node  链表的指定的节点
     * @param value 要插入的值
     */
    public void insertBefore(Node<T> node, T value) {
        if (null == node) {
            return;
        }

        if (null == head) {
            insertToHead(value);
            return;
        }

        Node<T> current = head;
        while (null != current && !Objects.equals(current.getNext(), node)) {
            current = current.getNext();
        }

        if (null == current) {
            return;
        }

        Node<T> newNode = new Node<>(value);
        newNode.setNext(node);
        current.setNext(newNode);
    }

    /**
     * 根据指定的值进行数据删除
     *
     * @param value 链上的值
     */
    public void deleteByValue(T value) {
        if (null == head) {
            insertToHead(value);
            return;
        }

        Node<T> current = head, prev = null;
        while (null != current && !Objects.equals(current.getItem(), value)) {
            prev = current;
            current = current.getNext();
        }

        // 没有找到匹配的值
        if (null == current) {
            return;
        }

        if (null == prev) {
            // 如果是删除头节点
            this.head = current.getNext();
        } else if (null == current.getNext()) {
            // 如果是删除尾节点
            prev.setNext(null);
        } else {
            current.setNext(current.getNext());
        }
    }

    /**
     * 判断是否为回文
     *
     * @return
     */
    public boolean palindrome() {
        return false;
    }

    public Node inverseLinkList(Node p) {
        return null;
    }

    /**
     * 打印结果
     */
    public void printAll() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.getItem() + " ");
            current = current.getNext();
        }
        System.out.println();
    }


    @Data
    @ToString
    class Node<E> {
        private E item;
        private Node<E> next;
        private Node<E> prev;

        public Node(E item) {
            this.item = item;
        }

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

        public Node(E item, Node<E> next, Node<E> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }


    public static void main(String[] args) {
        SinglyLinkedList<Integer> linkedList = new SinglyLinkedList<>();

        // 只有头节点
        IntStream.iterate(1, idx -> idx + 2).limit(10).forEach(linkedList::insertTail);
        linkedList.printAll();
        System.out.println("============= 我是分隔符 =============");
        linkedList.deleteByValue(1);
        linkedList.printAll();

        System.out.println("============= 我是分隔符 =============");
        linkedList.deleteByValue(19);
        linkedList.insertToHead(0);
        linkedList.printAll();

        System.out.println("============= 我是分隔符 =============");
        Optional.ofNullable(linkedList.findByIndex(3)).ifPresent(node -> {
            linkedList.insertBefore(node, 6);
            linkedList.insertAfter(node, 8);
        });
        linkedList.printAll();
    }
}

