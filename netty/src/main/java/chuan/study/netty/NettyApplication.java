package chuan.study.netty;

import chuan.study.netty.hello.NettyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动入口
 *
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-05-13
 */
@Slf4j
@SpringBootApplication
public class NettyApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        NettyClient.sendMessage();
    }
}