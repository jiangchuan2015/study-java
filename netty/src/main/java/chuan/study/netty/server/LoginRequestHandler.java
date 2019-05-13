package chuan.study.netty.server;

import chuan.study.netty.protocol.request.LoginRequestPacket;
import chuan.study.netty.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket request) throws Exception {
        log.info("收到客户端登录[{}]请求......", request);

        LoginResponsePacket responsePacket;
        if (StringUtils.equalsIgnoreCase(request.getUsername(), "admin")) {
            responsePacket = new LoginResponsePacket(true, "登录成功");
        } else {
            responsePacket = new LoginResponsePacket(false, "用户名密码错误。");
        }

        // 登录响应
        responsePacket.setVersion(request.getVersion());
        ctx.channel().writeAndFlush(responsePacket);
    }
}
