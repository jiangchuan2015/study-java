package chuan.study.concurrent.hello;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 模拟营业大厅叫号机
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-18
 */
@Slf4j
public class TicketWindow {
    public static void main(String[] args) {
        final int max = 1000;
        final TicketTask task = new TicketTask(max);
        IntStream.range(1, max).forEach(idx -> new Thread(task, leftPad(idx) + "号窗口").start());
    }

    private static String leftPad(int index) {
        return StringUtils.leftPad(index + "", 5, '0');
    }

    /**
     * 叫号机线程
     */
    static class TicketTask implements Runnable {
        private int max = 50;
        private int index = 1;

        public TicketTask(int max) {
            this.max = max;
        }

        @Override
        public void run() {
            while (index <= max) {
                log.error("号码是: {}", index++);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }
}
