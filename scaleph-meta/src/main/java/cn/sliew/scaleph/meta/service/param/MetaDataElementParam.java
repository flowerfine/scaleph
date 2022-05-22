package cn.sliew.scaleph.meta.service.param;

import cn.sliew.scaleph.common.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetaDataElementParam extends PaginationParam {
    @ApiModelProperty(value = "数据元标识")
    private String elementCode;

    @ApiModelProperty(value = "数据元名称")
    private String elementName;
}
