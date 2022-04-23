package com.liyu.breeze.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统配置信息表
 * </p>
 *
 * @author liyu
 * @since 2021-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_system_config")
@ApiModel(value="SystemConfig对象", description="系统配置信息表")
public class SystemConfig extends BaseDO {

    private static final long serialVersionUID = -5437539010004884444L;

    @ApiModelProperty(value = "配置编码")
    private String cfgCode;

    @ApiModelProperty(value = "配置信息")
    private String cfgValue;


}
