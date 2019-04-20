package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 模拟机票查询
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-20
 */
@Slf4j
public class FightQuery {
    private static final List<String> FIGHT_COMPANY = Arrays.asList("国航", "东航", "海航", "厦航");
    private static final String FROM = "成都", TO = "上海";

    public static void main(String[] args) {
        // 创建线程
        List<FightQueryTask> tasks = FIGHT_COMPANY.stream()
                .map(company -> new FightQueryTask(company, FROM, TO))
                .collect(Collectors.toList());

        // 启动线程
        tasks.forEach(Thread::start);

        // 等待所有线程结束
        tasks.forEach(task -> {
            try {
                task.join();
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        });

        // 获取结果
        List<String> results = tasks.stream().map(FightQueryTask::get).collect(Collectors.toList());
        System.out.println("=============== Results ===============");
        results.forEach(System.out::println);
    }


    /**
     * 内部查询线程
     */
    @Slf4j
    static class FightQueryTask extends Thread implements Supplier<String> {
        private final String from;
        private final String to;
        private String value = "";

        private FightQueryTask(String airCompany, String from, String to) {
            super(airCompany);
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            try {
                log.info("[{}] query from {} to {}", getName(), from, to);
                int randomValue = ThreadLocalRandom.current().nextInt(10);

                TimeUnit.SECONDS.sleep(randomValue);
                this.value = String.format("[%s] - Query Time: %sms", getName(), randomValue);

                log.info("The air company: [{}] query successful, took {}ms", getName(), randomValue);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        @Override
        public String get() {
            return this.value;
        }
    }
}

