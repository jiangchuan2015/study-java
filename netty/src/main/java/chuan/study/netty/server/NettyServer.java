package chuan.study.netty.server;

import chuan.study.netty.Constants;
import chuan.study.netty.codec.PacketDecoder;
import chuan.study.netty.codec.PacketEncoder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

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
public class NettyServer {
    /**
     * 手动创建线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(2,
            10, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("SERVER-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        createServer();
    }

    /**
     * 创建服务
     */
    private static void createServer() {
        // 启动服务监听
        new ServerBootstrap()
                // serverGroup, clientGroup
                .group(new NioEventLoopGroup(1, THREAD_POOL), new NioEventLoopGroup(1, THREAD_POOL))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)

                // 用于处理新连接数据的读写处理逻辑
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());

                        // 重构前的方式方式
                        // ch.pipeline().addLast(new ServerHandler());
                    }
                })
                .bind(Constants.SERVER_PORT)
                .addListener(future -> log.info("端口[{}]绑定{}!", Constants.SERVER_PORT, future.isSuccess() ? "成功" : "失败"));
    }
}
