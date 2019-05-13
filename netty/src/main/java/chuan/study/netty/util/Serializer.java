package chuan.study.netty.util;

import chuan.study.netty.protocol.BasePacket;
import chuan.study.netty.protocol.CommandEnum;
import chuan.study.netty.protocol.request.LoginRequestPacket;
import chuan.study.netty.protocol.request.MessageRequestPacket;
import chuan.study.netty.protocol.response.LoginResponsePacket;
import chuan.study.netty.protocol.response.MessageResponsePacket;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
public class Serializer {
    private static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<CommandEnum, Class<? extends BasePacket>> PACKET_TYPES = new HashMap<CommandEnum, Class<? extends BasePacket>>() {{
        put(CommandEnum.LOGIN_REQUEST, LoginRequestPacket.class);
        put(CommandEnum.LOGIN_RESPONSE, LoginResponsePacket.class);

        put(CommandEnum.MESSAGE_REQUEST, MessageRequestPacket.class);
        put(CommandEnum.MESSAGE_RESPONSE, MessageResponsePacket.class);
    }};

    public static void encode(BasePacket packet, ByteBuf outByteBuf) {
        // 1. 序列化 java 对象
        byte[] bytes = JSON.toJSONBytes(packet);

        // 2. 实际编码过程
        outByteBuf.writeInt(MAGIC_NUMBER);
        outByteBuf.writeByte(packet.getVersion());
        outByteBuf.writeInt(packet.getCommand().getCode());
        outByteBuf.writeInt(bytes.length);
        outByteBuf.writeBytes(bytes);
    }

    public static ByteBuf encode(BasePacket packet) {
        return encode(ByteBufAllocator.DEFAULT, packet);
    }

    public static ByteBuf encode(ByteBufAllocator allocator, BasePacket packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = Objects.isNull(allocator)
                ? ByteBufAllocator.DEFAULT.ioBuffer()
                : allocator.ioBuffer();

        // 2. 序列化 Java 对象
        byte[] bytes = JSON.toJSONBytes(packet);

        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeInt(packet.getCommand().getCode());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public static <T extends BasePacket> T decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 指令
        CommandEnum command = CommandEnum.parse(byteBuf.readInt());
        if (Objects.isNull(command)) {
            return null;
        }

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends BasePacket> requestType = PACKET_TYPES.get(command);
        if (Objects.nonNull(requestType)) {
            return JSON.parseObject(bytes, requestType);
        }
        return null;
    }

    public static void main(String[] args) {
        LoginRequestPacket packet = new LoginRequestPacket(UUID.randomUUID().toString(), "admin", "123456");
        ByteBuf byteBuf = Serializer.encode(packet);
        System.out.println(byteBuf);

        LoginRequestPacket loginPacket = Serializer.decode(byteBuf);
        System.out.println(loginPacket);
    }
}
