package cn.sliew.scaleph.service.dto.di;

import cn.sliew.scaleph.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
@ApiModel(value = "目录信息", description = "数据集成-项目目录")
public class DiDirectoryDTO extends BaseDTO {

    private static final long serialVersionUID = -5411947916873915670L;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @NotBlank
    @Length(min = 1, max = 32)
    @ApiModelProperty(value = "目录名称")
    private String directoryName;

    @ApiModelProperty(value = "上级目录id")
    private Long pid;

    @ApiModelProperty(value = "路径")
    private String fullPath;


}
