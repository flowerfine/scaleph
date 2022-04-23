package com.liyu.breeze.service.dto.admin;

import com.liyu.breeze.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户操作日志
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "操作日志信息", description = "用户操作日志")
public class LogActionDTO extends BaseDTO {

    private static final long serialVersionUID = 8003025002075099514L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    private Date actionTime;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "操作接口地址")
    private String actionUrl;

    @ApiModelProperty(value = "会话token字符串")
    private String token;

    @ApiModelProperty(value = "客户端信息")
    private String clientInfo;

    @ApiModelProperty(value = "操作系统信息")
    private String osInfo;

    @ApiModelProperty(value = "浏览器信息")
    private String browserInfo;

    @ApiModelProperty(value = "接口执行信息，包含请求结果")
    private String actionInfo;


}
