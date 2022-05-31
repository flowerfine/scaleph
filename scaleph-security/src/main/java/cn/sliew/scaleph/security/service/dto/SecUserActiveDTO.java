package cn.sliew.scaleph.security.service.dto;

import java.util.Date;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户邮箱激活日志表
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户激活码信息", description = "用户邮箱激活日志表")
public class SecUserActiveDTO extends BaseDTO {

    private static final long serialVersionUID = 8583076330823769080L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "激活码")
    private String activeCode;

    @ApiModelProperty(value = "激活码过期时间戳")
    private Long expiryTime;

    @ApiModelProperty(value = "激活时间")
    private Date activeTime;


}
