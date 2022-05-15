package cn.sliew.scaleph.dao.entity.master.di;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-项目目录
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_directory")
@ApiModel(value = "DiDirectory对象", description = "数据集成-项目目录")
public class DiDirectory extends BaseDO {

    private static final long serialVersionUID = 4689210168757550044L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "目录名称")
    private String directoryName;

    @ApiModelProperty(value = "上级目录id")
    private Long pid;


}
