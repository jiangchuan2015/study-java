package chuan.study.concurrent.jvmtest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-02-11
 */
@Slf4j
public class Shop {
    public static void main(String[] args) {
        Inner[] inners = Stream.iterate(1, i -> i++).limit(10).map(idx -> new Inner()).toArray(Inner[]::new);

        long start = System.currentTimeMillis();
        int count1 = 0, count2 = 0;
        for (Inner inner : inners) {
            Pair<Integer, Integer> counter = inner.getCount();
            count1 += counter.getLeft();
            count2 += counter.getRight();
        }
        log.error("1. 计算结果：{} - {}, 用时：{}ms", count1, count2, (System.currentTimeMillis() - start));


        start = System.currentTimeMillis();
        count1 = 0;
        count2 = 0;
        Executor executor = Executors.newCachedThreadPool();
        List<CompletableFuture<Pair<Integer, Integer>>> futures = Stream.of(inners)
                .map(inner -> CompletableFuture.supplyAsync(inner::getCount, executor))
                .collect(Collectors.toList());

        List<Pair<Integer, Integer>> counts = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        for (Pair<Integer, Integer> counter : counts) {
            count1 += counter.getLeft();
            count2 += counter.getRight();
        }
        log.error("2. 计算结果：{} - {}, 用时：{}ms", count1, count2, (System.currentTimeMillis() - start));
    }

    static class Inner {
        Pair<Integer, Integer> getCount() {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Pair.of(100, 200);
        }
    }
}
