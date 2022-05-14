package cn.sliew.breeze.dao.entity.master.di;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-项目信息
 * </p>
 *
 * @author liyu
 * @since 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_project")
@ApiModel(value="DiProject对象", description="数据集成-项目信息")
public class DiProject extends BaseDO {

    private static final long serialVersionUID = 6755304433208751192L;

    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
