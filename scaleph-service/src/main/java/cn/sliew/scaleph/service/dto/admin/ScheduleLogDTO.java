package cn.sliew.scaleph.service.dto.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.service.vo.DictVO;
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
@ApiModel(value = "定时任务日志信息", description = "定时任务运行日志表")
public class ScheduleLogDTO extends BaseDTO {

    private static final long serialVersionUID = 1976884925111874156L;

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
    private DictVO result;

    public ScheduleLogDTO appendLog(String raw) {
        StringBuilder builder = new StringBuilder(StrUtil.blankToDefault(this.traceLog, ""));
        builder.append(DateUtil.format(new Date(), Constants.MS_DATETIME_FORMAT))
                .append("\t")
                .append(raw)
                .append("\n");
        this.setTraceLog(builder.toString());
        return this;
    }

}
