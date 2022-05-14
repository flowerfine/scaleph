package cn.sliew.scalegh.dao.entity.master.meta;

import cn.sliew.scalegh.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-参考数据类型
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meta_data_set_type")
@ApiModel(value="MetaDataSetType对象", description="元数据-参考数据类型")
public class MetaDataSetType extends BaseDO {

    private static final long serialVersionUID = 5820268887218510474L;

    @ApiModelProperty(value = "参考数据类型编码")
    private String dataSetTypeCode;

    @ApiModelProperty(value = "参考数据类型名称")
    private String dataSetTypeName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
