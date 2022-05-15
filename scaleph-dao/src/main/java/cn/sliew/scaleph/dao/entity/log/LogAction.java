package cn.sliew.scaleph.dao.entity.log;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户操作日志
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_log_action")
@ApiModel(value = "LogAction对象", description = "用户操作日志")
public class LogAction extends BaseDO {

    private static final long serialVersionUID = 5083227320169178703L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    private Date actionTime;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "操作接口地址")
    private String actionUrl;

    @ApiModelProperty(value = "会话token字符串")
    private String token;

    @ApiModelProperty(value = "客户端信息")
    private String clientInfo;

    @ApiModelProperty(value = "操作系统信息")
    private String osInfo;

    @ApiModelProperty(value = "浏览器信息")
    private String browserInfo;

    @ApiModelProperty(value = "接口执行信息，包含请求结果")
    private String actionInfo;


}
