package cn.sliew.breeze.dao.entity.master.meta;

import cn.sliew.breeze.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-数据元信息
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "meta_data_element", resultMap = "MetaDataElementMap")
@ApiModel(value = "MetaDataElement对象", description = "元数据-数据元信息")
public class MetaDataElement extends BaseDO {

    private static final long serialVersionUID = -4396296342497985370L;

    @ApiModelProperty(value = "数据元标识")
    private String elementCode;

    @ApiModelProperty(value = "数据元名称")
    private String elementName;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "长度")
    private Long dataLength;

    @ApiModelProperty(value = "数据精度，有效位")
    private Integer dataPrecision;

    @ApiModelProperty(value = "小数位数")
    private Integer dataScale;

    @ApiModelProperty(value = "是否可以为空,1-是;0-否")
    private String nullable;

    @ApiModelProperty(value = "默认值")
    private String dataDefault;

    @ApiModelProperty(value = "最小值")
    private String lowValue;

    @ApiModelProperty(value = "最大值")
    private String highValue;

    @ApiModelProperty(value = "参考数据类型id")
    private Long dataSetTypeId;

    @TableField(exist = false)
    private MetaDataSetType dataSetType;
}
