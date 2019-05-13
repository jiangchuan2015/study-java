package chuan.study.netty.server;

import chuan.study.netty.protocol.BasePacket;
import chuan.study.netty.protocol.request.LoginRequestPacket;
import chuan.study.netty.protocol.request.MessageRequestPacket;
import chuan.study.netty.protocol.response.LoginResponsePacket;
import chuan.study.netty.protocol.response.MessageResponsePacket;
import chuan.study.netty.util.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
@Deprecated
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        BasePacket packet = Serializer.decode(byteBuf);

        if (packet instanceof LoginRequestPacket) {
            log.info("收到客户端登录[{}]请求......", packet);

            // TODO 登录流程, 校验证用户名密码
            LoginRequestPacket requestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket responsePacket;
            if (StringUtils.equalsIgnoreCase(requestPacket.getUsername(), "admin")) {
                responsePacket = new LoginResponsePacket(true, "登录成功");
            } else {
                responsePacket = new LoginResponsePacket(false, "用户名密码错误。");
            }

            // 登录响应
            ctx.channel().writeAndFlush(Serializer.encode(ctx.alloc(), responsePacket));
        } else if (packet instanceof MessageRequestPacket) {
            // 客户端发来消息
            MessageRequestPacket requestPacket = ((MessageRequestPacket) packet);
            log.info("收到客户端消息: {}", requestPacket.getMessage());

            ctx.channel().writeAndFlush(Serializer.encode(ctx.alloc(), MessageResponsePacket.builder().message("服务端回复【" + requestPacket.getMessage() + "】").build()));
        }
    }
}
