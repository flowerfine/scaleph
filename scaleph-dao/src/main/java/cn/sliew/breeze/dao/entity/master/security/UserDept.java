package cn.sliew.breeze.dao.entity.master.security;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户和部门关联表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_dept")
@ApiModel(value = "UserDept对象", description = "用户和部门关联表")
public class UserDept extends BaseDO {

    private static final long serialVersionUID = 8015344228302783975L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;


}
