package chuan.study.netty;

import java.nio.charset.Charset;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
public interface Constants {
    /**
     * 监听的服务器地址
     */
    String SERVER_HOST = "127.0.0.1";


    /**
     * 监听端口
     */
    int SERVER_PORT = 8000;

    /**
     * 系统默认编码
     */
    String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 系统默认编码
     */
    Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
}
