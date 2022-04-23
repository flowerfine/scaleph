package com.liyu.breeze.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 定时任务运行日志表
 * </p>
 *
 * @author liyu
 * @since 2021-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_schedule_log")
@ApiModel(value = "ScheduleLog对象", description = "定时任务运行日志表")
public class ScheduleLog extends BaseDO {

    private static final long serialVersionUID = -2239522121730705579L;

    @ApiModelProperty(value = "任务组")
    private String taskGroup;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "日志内容明细")
    private String traceLog;

    @ApiModelProperty(value = "任务结果")
    private String result;


}
