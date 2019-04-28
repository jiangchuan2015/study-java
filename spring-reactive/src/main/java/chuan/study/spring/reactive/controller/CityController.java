package chuan.study.spring.reactive.controller;

import chuan.study.spring.reactive.domain.City;
import chuan.study.spring.reactive.validation.CityInput;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-24
 */

@RestController
@RequestMapping("/city")
public class CityController implements MeterBinder {
    private final ReactiveRedisTemplate reactiveRedisTemplate;
    private Counter counter = null;

    @Autowired
    public CityController(ReactiveRedisTemplate reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<City> findById(@PathVariable("id") Long id) {
        counter.increment();
        ReactiveValueOperations<String, City> operations = reactiveRedisTemplate.opsForValue();
        return operations.get(generateCacheKey(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<City> create(@RequestBody @Validated({CityInput.class}) City city, BindingResult validResult) {
        if (validResult.hasErrors()) {
            throw new IllegalArgumentException("参数不正确");
        }
        ReactiveValueOperations<String, City> operations = reactiveRedisTemplate.opsForValue();
        return operations.getAndSet(generateCacheKey(city.getId()), city);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Long> delete(@PathVariable("id") Long id) {
        return reactiveRedisTemplate.delete(generateCacheKey(id));
    }


    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("controller.city.count");
    }

    private String generateCacheKey(Long id) {
        return "city:" + id;
    }


}
