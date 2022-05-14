package cn.sliew.scalegh.service.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

/**
 * 前端job graph
 *
 * @author gleiyu
 */
@Data
@ApiModel(value = "作业图对象", description = "作业图信息")
public class JobGraphVO {
    private String id;
    private String shape;
    private Map<String, Integer> position;
    private Map<String, Object> data;
    private EdgeNodeVO source;
    private EdgeNodeVO target;
}