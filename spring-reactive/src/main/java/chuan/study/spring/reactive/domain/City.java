package chuan.study.spring.reactive.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chuan.jiang <jctr@qq.com>
 * @version 1.0.0
 * @date 2019-04-24
 */
@Data
public class City implements Serializable {
    /**
     * 城市编号
     */
    private Long id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
