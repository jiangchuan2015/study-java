package chuan.study.spring.reactive.support;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-28
 */
@Component
public class CustomIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.up()
                .withDetail("now", System.currentTimeMillis())
                .withDetail("message", "服务正常")
                .build();
    }
}
