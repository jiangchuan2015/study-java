package chuan.study.spring.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-28
 */
@Slf4j
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Resource resource = new ClassPathResource("custom.yaml");
        try {
            PropertySource ps = loader.load("YetAnotherPropertiesFile", resource).get(0);
            propertySources.addFirst(ps);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
