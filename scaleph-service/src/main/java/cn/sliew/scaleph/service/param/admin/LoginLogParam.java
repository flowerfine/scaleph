package cn.sliew.scaleph.service.param.admin;

import cn.sliew.scaleph.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginLogParam extends PaginationParam {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "登录时间")
    private Date loginTime;
}
