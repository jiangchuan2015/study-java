package chuan.study.netty.client;

import chuan.study.netty.Constants;
import chuan.study.netty.codec.PacketDecoder;
import chuan.study.netty.codec.PacketEncoder;
import chuan.study.netty.protocol.request.MessageRequestPacket;
import chuan.study.netty.util.Serializer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Scanner;
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
public class NettyClient {
    /**
     * 连接服务器重试次数
     */
    private static final int MAX_RETRY = 5;

    /**
     * 手动创建线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(2,
            10, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("CLIENT-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        createClient();
    }

    /**
     * 创建客户端连接
     */
    private static void createClient() {
        Bootstrap bootstrap = new Bootstrap()
                // 1.指定线程模型
                .group(new NioEventLoopGroup(1, THREAD_POOL))

                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)

                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());

                        // 重构前的方式方式
                        // ch.pipeline().addLast(new ClientHandler("admin", "123456"));
                    }
                });

        // 建立连接，并重试
        connect(bootstrap, Constants.SERVER_HOST, Constants.SERVER_PORT, MAX_RETRY);
    }

    /**
     * 连接服务器，并重试
     *
     * @param bootstrap netty 客户端
     * @param host      服务器主机
     * @param port      服务器端口
     * @param retry     重试次数
     */
    private static void connect(final Bootstrap bootstrap, final String host, final int port, final int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("连接成功，启动控制台线程......");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                log.error("重试({})次后仍然不成功，终止重试!!!", MAX_RETRY);
            } else {
                // 第几次重连
                int times = (MAX_RETRY - retry) + 1;

                // 本次重连的间隔
                int delay = 1 << times;
                log.warn("连接失败，第{}次重试......", times);

                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    /**
     * 等待客户端输入消息
     *
     * @param channel 连接通道
     */
    private static void startConsoleThread(Channel channel) {
        final Attribute<Boolean> loginAttr = channel.attr(Constants.LOGIN_ATTRIBUTE);
        THREAD_POOL.execute(() -> {
            while (!Thread.interrupted()) {
                // 如果登录成功
                if (Optional.ofNullable(loginAttr).map(Attribute::get).orElse(false)) {
                    System.out.println("输入消息发送至服务端: ");
                    MessageRequestPacket packet = new MessageRequestPacket(new Scanner(System.in).nextLine());
                    channel.writeAndFlush(Serializer.encode(channel.alloc(), packet));
                }
            }
        });
    }
}
