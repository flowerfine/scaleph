package cn.sliew.scaleph.system.service.dto;

import cn.sliew.scaleph.common.dto.BaseDTO;
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
@ApiModel(value = "系统配置信息", description = "系统配置信息表")
public class SystemConfigDTO extends BaseDTO {

    private static final long serialVersionUID = -7747131256786691001L;

    @ApiModelProperty(value = "配置编码")
    private String cfgCode;

    @ApiModelProperty(value = "配置信息")
    private String cfgValue;


}
