package cn.sliew.scalegh.dao.entity.master.di;

import cn.sliew.scalegh.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业参数
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job_attr")
@ApiModel(value="DiJobAttr对象", description="数据集成-作业参数")
public class DiJobAttr extends BaseDO {

    private static final long serialVersionUID = 1709354370837885026L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "作业参数类型")
    private String jobAttrType;

    @ApiModelProperty(value = "作业参数key")
    private String jobAttrKey;

    @ApiModelProperty(value = "作业参数value")
    private String jobAttrValue;


}
