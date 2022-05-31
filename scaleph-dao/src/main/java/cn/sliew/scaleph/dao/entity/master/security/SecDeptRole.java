package cn.sliew.scaleph.dao.entity.master.security;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 部门角色关联表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sec_dept_role")
@ApiModel(value = "SecDeptRole对象", description = "部门角色关联表")
public class SecDeptRole extends BaseDO {

    private static final long serialVersionUID = -2427411241694222941L;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;


}
