package chuan.study.netty.old;

import chuan.study.netty.Constants;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class Client {
    /**
     * 手动创建线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(2,
            10, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("CLIENT-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        THREAD_POOL.execute(() -> {
            try {
                Socket socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
                while (true) {
                    try {
                        String message = LocalTime.now() + ": " + RandomStringUtils.randomAlphabetic(16);
                        log.info("发送消息: {}", message);
                        socket.getOutputStream().write(message.getBytes());
                        TimeUnit.SECONDS.sleep(2);
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }
}
