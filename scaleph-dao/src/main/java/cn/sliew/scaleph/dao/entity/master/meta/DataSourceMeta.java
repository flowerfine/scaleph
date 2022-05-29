package cn.sliew.scaleph.dao.entity.master.meta;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-数据源连接信息
 * </p>
 *
 * @author liyu
 * @since 2021-10-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meta_datasource")
@ApiModel(value = "DatasourceMeta对象", description = "元数据-数据源连接信息")
public class DataSourceMeta extends BaseDO {

    private static final long serialVersionUID = -1697296662006060595L;

    @ApiModelProperty(value = "数据源名称")
    private String datasourceName;

    @ApiModelProperty(value = "数据源类型")
    private String datasourceType;

    @ApiModelProperty(value = "数据源连接类型")
    private String connectionType;

    @ApiModelProperty(value = "主机地址")
    private String hostName;

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "备注描述")
    private String remark;

    @ApiModelProperty(value = "属性信息")
    private String props;


}
