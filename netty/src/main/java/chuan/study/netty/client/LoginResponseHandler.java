package chuan.study.netty.client;

import chuan.study.netty.Constants;
import chuan.study.netty.protocol.request.LoginRequestPacket;
import chuan.study.netty.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端开始登录......");

        // 写数据
        ctx.channel().writeAndFlush(LoginRequestPacket.builder()
                .userId(UUID.randomUUID().toString())
                .username("admin")
                .password("123456")
                .build());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket response) {
        if (response.isSuccess()) {
            log.info("客户端登录成功");
            ctx.channel().attr(Constants.LOGIN_ATTRIBUTE).set(true);
        } else {
            log.warn("客户端登录失败，原因: {}", response.getMessage());
        }
    }
}
