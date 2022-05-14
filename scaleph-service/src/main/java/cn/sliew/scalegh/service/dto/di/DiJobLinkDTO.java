package cn.sliew.scalegh.service.dto.di;

import cn.sliew.scalegh.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业连线
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业步骤连线信息", description = "数据集成-作业连线")
public class DiJobLinkDTO extends BaseDTO {

    private static final long serialVersionUID = 7751624449715348321L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "作业连线编码")
    private String linkCode;

    @ApiModelProperty(value = "源步骤编码")
    private String fromStepCode;

    @ApiModelProperty(value = "目标步骤编码")
    private String toStepCode;


}
