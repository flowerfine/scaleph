package com.liyu.breeze.service.param.admin;

import com.liyu.breeze.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActionLogParam extends PaginationParam {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    private Date actionTime;
}
