package chuan.study.netty.hello;

import chuan.study.netty.Constants;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class NettyClient {
    /**
     * 数字格式
     */
    private static final DecimalFormat NUMBER_FORMATTER = new DecimalFormat("000,000");

    /**
     * 手动创建线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(2,
            10, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("CLIENT-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        sendMessage();
    }

    /**
     * 发送消息
     */
    public static void sendMessage() {
        Bootstrap bootstrap = new Bootstrap()
                // 1.指定线程模型
                .group(new NioEventLoopGroup(1, THREAD_POOL))

                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)

                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new FirstClientHandler());
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        // 4.建立连接
        Channel channel = bootstrap
                .connect(Constants.SERVER_HOST, Constants.SERVER_PORT)
                .addListener(future -> log.info("连接服务器端口: {} {}", Constants.SERVER_PORT, future.isSuccess() ? "成功" : "失败"))
                .channel();

        IntStream.iterate(1, idx -> idx + 1).limit(100_000).forEach(idx -> {
            String message = String.format("%s - %s: %s", NUMBER_FORMATTER.format(idx), LocalTime.now(), RandomStringUtils.randomAlphabetic(16));
            log.info("发送消息: {}", message);
            channel.writeAndFlush(message);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }


    static class FirstClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext context) {
            log.info("客户端正在写数据...");

            // 1. 获取二进制抽象 ByteBuf
            ByteBuf buffer = context.alloc().buffer();

            // 2. 准备数据，指定字符串的字符集为 utf-8
            byte[] bytes = "你好，Netty".getBytes(Constants.DEFAULT_CHARSET);

            // 3. 填充数据到 ByteBuf
            buffer.writeBytes(bytes);

            // 4. 写数据
            context.channel().writeAndFlush(buffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext context, Object message) {
            ByteBuf buf = (ByteBuf) message;
            log.info("客户端读到数据 -> {}", buf.toString(Constants.DEFAULT_CHARSET));
        }
    }

}
