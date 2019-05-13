package chuan.study.netty.hello;

import chuan.study.netty.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        // 启动服务监听
        new ServerBootstrap()
                // serverGroup, clientGroup
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                // 用于处理服务端启动过程中的一些逻辑
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        log.info("服务端启动中，监听端口: {}", Constants.SERVER_PORT);
                    }
                })
                // 用于处理新连接数据的读写处理逻辑
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new FirstServerHandler());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                log.info("接收到的消息: {}", msg);
                            }
                        });
                    }
                })
                .bind(Constants.SERVER_PORT)
                .addListener(future -> log.info("绑定端口: {} {}", Constants.SERVER_PORT, future.isSuccess() ? "成功" : "失败"));
    }


    /**
     * 服务端，每收到一条消息都进行回复
     */
    static class FirstServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext context, Object message) {
            ByteBuf buf = (ByteBuf) message;
            log.info("服务端接收到数据 -> {}", buf.toString(Constants.DEFAULT_CHARSET));
            log.info("服务端回复信息...");

            // 1. 获取二进制抽象 ByteBuf
            ByteBuf buffer = context.alloc().buffer();

            /*
            // 2. 准备数据，指定字符串的字符集为 utf-8
            byte[] bytes = "欢迎来到 Netty 的世界!".getBytes(Constants.DEFAULT_CHARSET);

            // 3. 填充数据到 ByteBuf
            buffer.writeBytes(bytes);
            */

            // 将原数据返回给客户端
            buffer.writeBytes(buf);

            // 4. 写数据
            context.channel().writeAndFlush(buffer);
        }
    }
}
