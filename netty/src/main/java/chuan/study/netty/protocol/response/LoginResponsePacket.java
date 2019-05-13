package chuan.study.netty.protocol.response;

import chuan.study.netty.protocol.BasePacket;
import chuan.study.netty.protocol.CommandEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponsePacket extends BasePacket {
    /**
     * 是否处理成功
     */
    private boolean success;

    /**
     * 处理后的消息
     */
    private String message;


    @Override
    public CommandEnum getCommand() {
        return CommandEnum.LOGIN_RESPONSE;
    }

    @Builder
    public LoginResponsePacket(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
