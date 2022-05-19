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
public class MetaDataMapParam extends PaginationParam {

    @ApiModelProperty(value = "源参考数据类型编码")
    private String srcDataSetTypeCode;

    @ApiModelProperty(value = "目标参考数据类型编码")
    private String tgtDataSetTypeCode;

    @ApiModelProperty(value = "源参考数据编码")
    private String srcDataSetCode;

    @ApiModelProperty(value = "目标参考数据编码")
    private String tgtDataSetCode;

    @ApiModelProperty(value = "自动映射")
    private boolean auto;
}
