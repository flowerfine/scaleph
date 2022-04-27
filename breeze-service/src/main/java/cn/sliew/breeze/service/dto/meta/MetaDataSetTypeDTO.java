package cn.sliew.breeze.service.dto.meta;

import cn.sliew.breeze.service.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @ApiModelProperty(value = "参考数据类型编码")
    private String dataSetTypeCode;

    @ApiModelProperty(value = "参考数据类型名称")
    private String dataSetTypeName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
