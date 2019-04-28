package chuan.study.spring.reactive.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @param exception Throwable
     * @return 统一异常处理信息
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Throwable.class)
    public String handleAuthenticationException(Throwable exception) {
        return exception.getMessage();
    }
}