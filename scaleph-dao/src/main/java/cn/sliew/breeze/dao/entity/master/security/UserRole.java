package cn.sliew.breeze.dao.entity.master.security;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_role")
@ApiModel(value = "UserRole对象", description = "用户角色关联表")
public class UserRole extends BaseDO {

    private static final long serialVersionUID = 8752486397778737688L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;


}
