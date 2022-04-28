package cn.sliew.breeze.service.dto.meta;

import cn.sliew.breeze.service.dto.BaseDTO;
import cn.sliew.breeze.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-参考数据
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MetaDataSet对象", description = "元数据-参考数据")
public class MetaDataSetDTO extends BaseDTO {

    private static final long serialVersionUID = -8370149134397925591L;

    @ApiModelProperty(value = "参考数据类型")
    private MetaDataSetTypeDTO dataSetType;

    @ApiModelProperty(value = "代码code")
    private String dataSetCode;

    @ApiModelProperty(value = "代码值")
    private String dataSetValue;

    @ApiModelProperty(value = "业务系统id")
    private MetaSystemDTO system;

    @ApiModelProperty(value = "是否标准参考数据")
    private DictVO isStandard;

    @ApiModelProperty(value = "备注")
    private String remark;

}
