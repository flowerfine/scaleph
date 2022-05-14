package cn.sliew.scalegh.service.param.admin;

import cn.sliew.scalegh.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author gleiyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleLogParam extends PaginationParam {
    @ApiModelProperty(value = "任务组")
    private String taskGroup;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "任务结果")
    private String result;
}
