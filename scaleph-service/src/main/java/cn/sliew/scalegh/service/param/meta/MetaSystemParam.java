package cn.sliew.scalegh.service.param.meta;

import cn.sliew.scalegh.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetaSystemParam extends PaginationParam {
    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "系统名称")
    private String systemName;
}
