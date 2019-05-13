package chuan.study.netty.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Getter
@AllArgsConstructor
public enum CommandEnum {
    /**
     * 登录
     */
    LOGIN_REQUEST(1, "登录"),
    LOGIN_RESPONSE(101, "登录"),

    /**
     * 发送消息
     */
    MESSAGE_REQUEST(2, "发送消息"),
    MESSAGE_RESPONSE(202, "发送消息"),
    ;

    private final static Map<Integer, CommandEnum> BY_CODE_MAP
            = Arrays.stream(CommandEnum.values()).collect(Collectors.toMap(CommandEnum::getCode, code -> code));

    private final static Map<String, CommandEnum> BY_NAME_MAP
            = Arrays.stream(CommandEnum.values()).collect(Collectors.toMap(code -> code.name().toLowerCase(), code -> code));


    private final int code;
    private final String desc;


    /**
     * @param code 代码
     * @return 转换出来的状态码
     */
    public static CommandEnum parse(Integer code) {
        return BY_CODE_MAP.get(code);
    }

    public static CommandEnum parse(Integer code, CommandEnum defaultState) {
        return BY_CODE_MAP.getOrDefault(code, defaultState);
    }

    /**
     * @param name 名字
     * @return 转换出来的状态码
     */
    public static CommandEnum parse(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return BY_NAME_MAP.get(name.trim().toLowerCase());
    }

    public static CommandEnum parse(String name, CommandEnum defaultState) {
        if (StringUtils.isBlank(name)) {
            return defaultState;
        }
        return BY_NAME_MAP.getOrDefault(name.trim().toLowerCase(), defaultState);
    }
}
