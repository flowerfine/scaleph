package cn.sliew.scaleph.dao.entity.master.security;

import java.util.List;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_role", resultMap = "RoleMap")
@ApiModel(value = "Role对象", description = "角色表")
public class Role extends BaseDO {

    private static final long serialVersionUID = 2621684597930016649L;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色类型")
    private String roleType;

    @ApiModelProperty(value = "角色状态")
    private String roleStatus;

    @ApiModelProperty(value = "角色备注")
    private String roleDesc;

    @ApiModelProperty(value = "权限信息")
    private List<Privilege> privileges;
}
