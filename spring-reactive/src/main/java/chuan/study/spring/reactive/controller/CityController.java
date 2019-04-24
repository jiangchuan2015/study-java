package chuan.study.spring.reactive.controller;

import chuan.study.spring.reactive.domain.City;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.MediaType;
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
@AllArgsConstructor
public class CityController {
    private final ReactiveRedisTemplate reactiveRedisTemplate;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<City> findById(@PathVariable("id") Long id) {
        ReactiveValueOperations<String, City> operations = reactiveRedisTemplate.opsForValue();
        return operations.get(generateCacheKey(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<City> create(@RequestBody City city) {
        ReactiveValueOperations<String, City> operations = reactiveRedisTemplate.opsForValue();
        return operations.getAndSet(generateCacheKey(city.getId()), city);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Long> delete(@PathVariable("id") Long id) {
        return reactiveRedisTemplate.delete(generateCacheKey(id));
    }

    private String generateCacheKey(Long id) {
        return "city:" + id;
    }

}
