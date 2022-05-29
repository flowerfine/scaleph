package cn.sliew.scaleph.core.di.service.dto;

import java.util.Date;

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "DiJobLog对象", description = "数据集成-作业运行日志")
public class DiJobLogDTO extends BaseDTO {

    private static final long serialVersionUID = -7082895493477850984L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目")
    private DiProjectDTO project;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "作业编码")
    private String jobCode;

    @ApiModelProperty(value = "执行集群id")
    private Long clusterId;

    @ApiModelProperty(value = "执行集群")
    private DiClusterConfigDTO cluster;

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
    private DictVO jobInstanceState;


}
