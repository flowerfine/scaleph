package cn.sliew.scalegh.service.dto.meta;

import cn.sliew.breeze.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.scalegh.service.dto.BaseDTO;
import cn.sliew.scalegh.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 元数据-数据元信息
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDataElement对象", description = "元数据-数据元信息")
public class MetaDataElementDTO extends BaseDTO {

    private static final long serialVersionUID = 6216584991752649574L;

    @NotBlank
    @Length(min = 1, max = 30)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "数据元标识")
    private String elementCode;

    @NotBlank
    @Length(max = 250)
    @ApiModelProperty(value = "数据元名称")
    private String elementName;

    @NotNull
    @ApiModelProperty(value = "数据类型")
    private DictVO dataType;

    @ApiModelProperty(value = "长度")
    private Long dataLength;

    @ApiModelProperty(value = "数据精度，有效位")
    private Integer dataPrecision;

    @ApiModelProperty(value = "小数位数")
    private Integer dataScale;

    @ApiModelProperty(value = "是否可以为空,1-是;0-否")
    private DictVO nullable;

    @ApiModelProperty(value = "默认值")
    private String dataDefault;

    @ApiModelProperty(value = "最小值")
    private String lowValue;

    @ApiModelProperty(value = "最大值")
    private String highValue;

    @ApiModelProperty(value = "参考数据类型")
    private MetaDataSetType dataSetType;

}
