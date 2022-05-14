package cn.sliew.scalegh.dao.entity.master.di;

import cn.sliew.scalegh.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job")
@ApiModel(value = "DiJob对象", description = "数据集成-作业信息")
public class DiJob extends BaseDO {

    private static final long serialVersionUID = -4141741507654897469L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "作业编码")
    private String jobCode;

    @ApiModelProperty(value = "作业名称")
    private String jobName;

    @ApiModelProperty(value = "作业目录")
    private Long directoryId;

    @ApiModelProperty(value = "作业类型 实时、离线")
    private String jobType;

    @ApiModelProperty(value = "负责人")
    private String jobOwner;

    @ApiModelProperty(value = "作业状态 草稿、发布、归档")
    private String jobStatus;

    @ApiModelProperty(value = "运行状态 停止、运行中、等待运行")
    private String runtimeState;

    @ApiModelProperty(value = "作业版本号")
    private Integer jobVersion;

    @ApiModelProperty(value = "集群id")
    private Long clusterId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
