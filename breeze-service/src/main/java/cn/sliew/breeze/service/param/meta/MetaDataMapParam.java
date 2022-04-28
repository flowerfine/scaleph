package cn.sliew.breeze.service.param.meta;

import cn.sliew.breeze.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetaDataMapParam extends PaginationParam {

    @ApiModelProperty(value = "原始数据代码")
    private Long srcDataSetId;

    @ApiModelProperty(value = "目标数据代码")
    private Long tgtDataSetId;
}
