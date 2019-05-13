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
public class MessageRequestPacket extends BasePacket {
    /**
     * 发送的消息
     */
    private String message;

    @Override
    public CommandEnum getCommand() {
        return CommandEnum.MESSAGE_REQUEST;
    }

    @Builder
    public MessageRequestPacket(String message) {
        this.message = message;
    }
}
