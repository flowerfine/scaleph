package com.liyu.breeze.service.dto.admin;

import com.liyu.breeze.service.dto.BaseDTO;
import com.liyu.breeze.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限信息", description = "权限表")
public class PrivilegeDTO extends BaseDTO {

    private static final long serialVersionUID = 9063779257828908860L;

    @ApiModelProperty(value = "权限标识")
    private String privilegeCode;

    @ApiModelProperty(value = "权限名称")
    private String privilegeName;

    @ApiModelProperty(value = "资源类型")
    private DictVO resourceType;

    @ApiModelProperty(value = "资源路径")
    private String resourcePath;

    @ApiModelProperty(value = "上级权限id")
    private Long pid;

}
