package cn.sliew.scaleph.security.service.dto;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户角色信息", description = "用户角色关联表")
public class SecUserRoleDTO extends BaseDTO {

    private static final long serialVersionUID = 2981576110763410657L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;
}
