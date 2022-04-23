package cn.sliew.breeze.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业步骤参数
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job_step_attr")
@ApiModel(value="DiJobStepAttr对象", description="数据集成-作业步骤参数")
public class DiJobStepAttr extends BaseDO {

    private static final long serialVersionUID = 9084640873843622607L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "步骤编码")
    private String stepCode;

    @ApiModelProperty(value = "步骤参数key")
    private String stepAttrKey;

    @ApiModelProperty(value = "步骤参数value")
    private String stepAttrValue;


}
