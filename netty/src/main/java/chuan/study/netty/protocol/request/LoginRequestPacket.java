package chuan.study.netty.protocol.request;

import chuan.study.netty.protocol.BasePacket;
import chuan.study.netty.protocol.CommandEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginRequestPacket extends BasePacket {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public CommandEnum getCommand() {
        return CommandEnum.LOGIN_REQUEST;
    }

    @Builder
    public LoginRequestPacket(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
