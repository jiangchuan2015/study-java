package chuan.study.netty.client;

import chuan.study.netty.Constants;
import chuan.study.netty.protocol.BasePacket;
import chuan.study.netty.protocol.request.LoginRequestPacket;
import chuan.study.netty.protocol.response.LoginResponsePacket;
import chuan.study.netty.protocol.response.MessageResponsePacket;
import chuan.study.netty.util.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

/**
 * 客户端消息处理器
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
@Deprecated
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final String username;
    private final String password;

    public ClientHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端开始登录......");

        // 创建登录对象
        LoginRequestPacket packet = LoginRequestPacket.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(password)
                .build();

        // 写数据
        ctx.channel().writeAndFlush(Serializer.encode(ctx.alloc(), packet));
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        BasePacket packet = Serializer.decode(byteBuf);
        if (Objects.isNull(packet) || Objects.isNull(packet.getCommand())) {
            log.warn("解析服务端返回信息有误！");
            return;
        }

        switch (packet.getCommand()) {
            case LOGIN_RESPONSE:
                LoginResponsePacket loginResponse = (LoginResponsePacket) packet;
                if (loginResponse.isSuccess()) {
                    log.info("客户端登录成功");
                    ctx.channel().attr(Constants.LOGIN_ATTRIBUTE).set(true);
                } else {
                    log.warn("客户端登录失败，原因: {}", loginResponse.getMessage());
                }
                break;
            case MESSAGE_RESPONSE:
                MessageResponsePacket messageResponse = (MessageResponsePacket) packet;
                log.info("收到服务端的消息: {}", messageResponse.getMessage());
                break;
            default:
                log.warn("未知命令");
                break;
        }
    }
}
