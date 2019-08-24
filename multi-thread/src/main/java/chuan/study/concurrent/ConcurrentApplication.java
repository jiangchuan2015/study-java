package chuan.study.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动入口
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-08
 */
@Slf4j
@EnableAsync
@SpringBootApplication
public class ConcurrentApplication implements ApplicationRunner {
    /**
     * 最大线程数量
     */
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentApplication.class, args);
        // new SpringApplicationBuilder(ConcurrentApplication.class)
        //        .web(WebApplicationType.NONE)
        //        .run(args);
    }

    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            // -%d不要少
            .setNameFormat("async-task-name-%d")
            .setDaemon(true)
            .build();

    @Bean("threadPool")
    public Executor threadPool() {
        return new ThreadPoolExecutor(MAX_THREADS,
                2 * MAX_THREADS, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                new ThreadFactoryBuilder().setNameFormat("async-task-%d").setDaemon(true).build(),
                (runnable, executor) -> log.error("Task is rejected!"));
    }


    @Async("threadPool")
    public void doTask() throws InterruptedException {
        System.out.println("MsgServer send A thread name->" + Thread.currentThread().getName());
        Long startTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);

        Long endTime = System.currentTimeMillis();
        System.out.println("MsgServer send A 耗时：" + (endTime - startTime));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.range(1, 6)
                .publishOn(Schedulers.elastic())
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return 10 / (i - 3);
//					return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE"),
                        s -> s.request(4)
                );
        Thread.sleep(2000);
    }


}