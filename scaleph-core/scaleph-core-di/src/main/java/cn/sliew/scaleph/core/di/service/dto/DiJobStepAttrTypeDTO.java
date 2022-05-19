package cn.sliew.scaleph.core.di.service.dto;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业步骤参数类型信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业步骤参数类型", description = "数据集成-作业步骤参数类型信息")
public class DiJobStepAttrTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 153877584223991187L;

    @ApiModelProperty(value = "步骤类型")
    private String stepType;

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "步骤参数key")
    private String stepAttrKey;

    @ApiModelProperty(value = "步骤参数默认值")
    private String stepAttrDefaultValue;

    @ApiModelProperty(value = "是否需要")
    private String isRequired;

    @ApiModelProperty(value = "步骤参数描述")
    private String stepAttrDescribe;


}
