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
public class DictTypeParam extends PaginationParam {
    @ApiModelProperty(value = "字典类型编码")
    private String dictTypeCode;

    @ApiModelProperty(value = "字典类型名称")
    private String dictTypeName;
}
