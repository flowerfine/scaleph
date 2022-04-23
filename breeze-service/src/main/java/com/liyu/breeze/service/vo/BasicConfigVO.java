package com.liyu.breeze.service.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "基础配置信息", description = "基础配置信息")
public class BasicConfigVO {
    @NotNull
    private String seatunnelHome;
}
