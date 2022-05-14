package cn.sliew.breeze.dao.entity.master.di;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 数据集成-作业步骤信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job_step")
@ApiModel(value = "DiJobStep对象", description = "数据集成-作业步骤信息")
public class DiJobStep extends BaseDO {

    private static final long serialVersionUID = -8131332626792290363L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "步骤编码")
    private String stepCode;

    @ApiModelProperty(value = "步骤标题")
    private String stepTitle;

    @ApiModelProperty(value = "步骤类型")
    private String stepType;

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "x坐标")
    private Integer positionX;

    @ApiModelProperty(value = "y坐标")
    private Integer positionY;

    @TableField(exist = false)
    @ApiModelProperty(value = "步骤属性信息")
    List<DiJobStepAttr> jobStepAttrList;
}
