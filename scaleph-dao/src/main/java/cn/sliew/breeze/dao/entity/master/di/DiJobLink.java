package cn.sliew.breeze.dao.entity.master.di;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("di_job_link")
@ApiModel(value = "DiJobLink对象", description = "数据集成-作业连线")
public class DiJobLink extends BaseDO {

    private static final long serialVersionUID = 8533125383197913516L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "作业连线编码")
    private String linkCode;

    @ApiModelProperty(value = "源步骤编码")
    private String fromStepCode;

    @ApiModelProperty(value = "目标步骤编码")
    private String toStepCode;


}
