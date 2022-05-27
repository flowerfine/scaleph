package cn.sliew.scaleph.meta.service.param;

import cn.sliew.scaleph.common.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaDatasourceParam extends PaginationParam {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "版本")
    private String version;
}
