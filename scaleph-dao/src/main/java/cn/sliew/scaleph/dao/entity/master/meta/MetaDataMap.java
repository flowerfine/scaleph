package cn.sliew.scaleph.dao.entity.master.meta;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-参考数据映射
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meta_data_map")
@ApiModel(value = "MetaDataMap对象", description = "元数据-参考数据映射")
public class MetaDataMap extends BaseDO {

    private static final long serialVersionUID = 1413344387211239856L;

    @TableField(exist = false)
    @ApiModelProperty(value = "源参考数据类型编码")
    private String srcDataSetTypeCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "源参考数据类型名称")
    private String srcDataSetTypeName;

    @ApiModelProperty(value = "源参考数据id")
    private Long srcDataSetId;

    @TableField(exist = false)
    @ApiModelProperty(value = "源参考数据编码")
    private String srcDataSetCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "源参考数据值")
    private String srcDataSetValue;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标参考数据类型编码")
    private String tgtDataSetTypeCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标参考数据类型名称")
    private String tgtDataSetTypeName;

    @ApiModelProperty(value = "目标参考数据id")
    private Long tgtDataSetId;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标参考数据编码")
    private String tgtDataSetCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标参考数据值")
    private String tgtDataSetValue;

    @ApiModelProperty(value = "备注")
    private String remark;
}
