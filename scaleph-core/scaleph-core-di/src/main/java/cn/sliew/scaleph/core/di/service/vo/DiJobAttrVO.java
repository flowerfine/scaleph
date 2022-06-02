package cn.sliew.scaleph.core.di.service.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "作业属性信息", description = "作业属性信息")
public class DiJobAttrVO {
    @NotNull
    @ApiModelProperty(value = "作业id")
    private Long jobId;
    @ApiModelProperty(value = "作业变量")
    private String jobAttr;
    @ApiModelProperty(value = "作业属性")
    private String jobProp;
    @ApiModelProperty(value = "引擎属性")
    private String engineProp;

}
