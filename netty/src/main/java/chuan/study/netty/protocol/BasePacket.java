package chuan.study.netty.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Data
public abstract class BasePacket {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    /**
     * 协议命令
     *
     * @return 命令
     */
    @JSONField(serialize = false)
    public abstract CommandEnum getCommand();
}
