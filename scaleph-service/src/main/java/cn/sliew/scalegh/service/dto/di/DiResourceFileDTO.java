package cn.sliew.scalegh.service.dto.di;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据集成-资源
 * </p>
 *
 * @author liyu
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DiResourceFile对象", description = "数据集成-资源")
public class DiResourceFileDTO extends BaseDTO {

    private static final long serialVersionUID = 7190312674832816172L;

    @NotNull
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @NotBlank
    @Length(max = 128)
    @ApiModelProperty(value = "资源名称")
    private String fileName;

    @ApiModelProperty(value = "资源类型")
    private String fileType;

    @ApiModelProperty(value = "资源路径")
    private String filePath;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    public void resolveFileType(String fileName) {
        if (StrUtil.isNotEmpty(fileName)) {
            if (fileName.lastIndexOf('.') != -1) {
                this.fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            }
        }
    }


}
