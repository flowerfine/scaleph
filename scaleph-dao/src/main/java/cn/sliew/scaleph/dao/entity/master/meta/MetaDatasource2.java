package cn.sliew.scaleph.dao.entity.master.meta;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据-数据源信息2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meta_datasource2")
@ApiModel(value = "MetaDatasource2对象", description = "元数据-数据源信息2")
public class MetaDatasource2 extends BaseDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "备注描述")
    private String remark;

    @ApiModelProperty(value = "数据源支持的属性")
    private String props;

    @ApiModelProperty(value = "数据源支持的额外属性。")
    private String additionalProps;


}
