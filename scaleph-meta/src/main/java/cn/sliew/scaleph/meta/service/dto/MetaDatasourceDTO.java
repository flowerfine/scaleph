package cn.sliew.scaleph.meta.service.dto;

import javax.validation.constraints.NotBlank;
import java.util.Map;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据-数据源信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDatasource对象", description = "元数据-数据源信息")
public class MetaDatasourceDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "数据源支持的属性")
    private Map<String, Object> props;

    @ApiModelProperty(value = "数据源支持的额外属性。")
    private Map<String, Object> additionalProps;

    @ApiModelProperty(value = "备注描述")
    private String remark;

}
