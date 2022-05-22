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
public class MetaDataSetTypeParam extends PaginationParam {

    @ApiModelProperty(value = "参考数据类型编码")
    private String dataSetTypeCode;

    @ApiModelProperty(value = "参考数据类型名称")
    private String dataSetTypeName;
}
