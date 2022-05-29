package cn.sliew.scaleph.log.service.param;

import java.util.Date;

import cn.sliew.scaleph.common.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogActionParam extends PaginationParam {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    private Date actionTime;
}
