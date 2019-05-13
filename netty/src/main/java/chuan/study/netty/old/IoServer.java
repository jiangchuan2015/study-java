package chuan.study.netty.old;

import chuan.study.netty.Constants;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class IoServer {
    /**
     * 手动创建线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(2,
            10, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("IO-SERVER-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);

        // (1) 接收新连接线程
        THREAD_POOL.execute(() -> {
            while (true) {
                try {
                    // (1) 阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();

                    // (2) 每一个新的连接都创建一个线程，负责读取数据
                    THREAD_POOL.execute(() -> {
                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            // (3) 按字节流方式读取数据
                            while ((len = inputStream.read(data)) != -1) {
                                log.info("接收到的消息: {}", new String(data, 0, len));
                            }
                        } catch (IOException ex) {
                            log.error(ex.getMessage(), ex);
                        }
                    });
                } catch (IOException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        });
    }
}
