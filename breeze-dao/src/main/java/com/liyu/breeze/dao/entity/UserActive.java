package com.liyu.breeze.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户邮箱激活日志表
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_active")
@ApiModel(value="UserActive对象", description="用户邮箱激活日志表")
public class UserActive extends BaseDO {

    private static final long serialVersionUID = 2629509779716126101L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "激活码")
    private String activeCode;

    @ApiModelProperty(value = "激活码过期时间戳")
    private Long expiryTime;

    @ApiModelProperty(value = "激活时间")
    private Date activeTime;


}
