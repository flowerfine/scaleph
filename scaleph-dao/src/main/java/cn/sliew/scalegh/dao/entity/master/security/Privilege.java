package cn.sliew.scalegh.dao.entity.master.security;

import cn.sliew.scalegh.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_privilege")
@ApiModel(value = "Privilege对象", description = "权限表")
public class Privilege extends BaseDO {

    private static final long serialVersionUID = 4366151466958631600L;

    @ApiModelProperty(value = "权限标识")
    private String privilegeCode;

    @ApiModelProperty(value = "权限名称")
    private String privilegeName;

    @ApiModelProperty(value = "资源类型")
    private String resourceType;

    @ApiModelProperty(value = "资源路径")
    private String resourcePath;

    @ApiModelProperty(value = "上级权限id")
    private Long pid;
}
