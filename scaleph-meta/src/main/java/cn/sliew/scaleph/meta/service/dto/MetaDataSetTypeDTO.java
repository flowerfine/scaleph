package cn.sliew.scaleph.meta.service.dto;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 元数据-参考数据类型
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDataSetType对象", description = "元数据-参考数据类型")
public class MetaDataSetTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 263121616621915206L;

    @NotBlank
    @Length(min = 1, max = 32)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "参考数据类型编码")
    private String dataSetTypeCode;

    @NotBlank
    @Length(max = 128)
    @ApiModelProperty(value = "参考数据类型名称")
    private String dataSetTypeName;

    @Length(max = 256)
    @ApiModelProperty(value = "备注")
    private String remark;


}
