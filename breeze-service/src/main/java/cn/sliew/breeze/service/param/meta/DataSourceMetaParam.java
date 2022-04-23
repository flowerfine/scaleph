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
public class DataSourceMetaParam extends PaginationParam {

    @ApiModelProperty("数据源名称")
    private String dataSourceName;

    @ApiModelProperty("数据源类型")
    private String dataSourceType;

    @ApiModelProperty("主机地址")
    private String hostName;

    @ApiModelProperty("数据库名称")
    private String databaseName;
}
