package cn.sliew.breeze.dao.entity.master.di;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 数据集成-作业运行日志
 * </p>
 *
 * @author liyu
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "di_job_log", resultMap = "DiJobLogMap")
@ApiModel(value = "DiJobLog对象", description = "数据集成-作业运行日志")
public class DiJobLog extends BaseDO {

    private static final long serialVersionUID = -8679804488509252540L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目")
    private DiProject project;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "作业编码")
    private String jobCode;

    @ApiModelProperty(value = "执行集群id")
    private Long clusterId;

    @TableField(exist = false)
    @ApiModelProperty(value = "执行集群")
    private DiClusterConfig cluster;

    @ApiModelProperty(value = "作业实例id")
    private String jobInstanceId;

    @ApiModelProperty(value = "作业日志URL")
    private String jobLogUrl;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "消耗时长-毫秒")
    private Long duration;

    @ApiModelProperty(value = "任务结果")
    private String jobInstanceState;


}
