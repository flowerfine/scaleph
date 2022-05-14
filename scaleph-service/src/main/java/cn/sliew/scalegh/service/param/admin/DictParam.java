package cn.sliew.scalegh.service.param.admin;

import cn.sliew.scalegh.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictParam extends PaginationParam {
    @ApiModelProperty(value = "字典类型编码")
    private String dictTypeCode;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "是否有效")
    private String isValid;
}
