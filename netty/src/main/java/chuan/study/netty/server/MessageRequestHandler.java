package chuan.study.netty.server;

import chuan.study.netty.protocol.request.MessageRequestPacket;
import chuan.study.netty.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket request) throws Exception {
        log.info("收到客户端消息: {}", request.getMessage());
        ctx.channel().writeAndFlush(MessageResponsePacket.builder().message("服务端回复【" + request.getMessage() + "】").build());
    }
}
