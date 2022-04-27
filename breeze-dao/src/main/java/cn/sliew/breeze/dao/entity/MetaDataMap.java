package cn.sliew.breeze.dao.entity;

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
@ApiModel(value="MetaDataMap对象", description="元数据-参考数据映射")
public class MetaDataMap extends BaseDO {

    private static final long serialVersionUID = 1413344387211239856L;

    @ApiModelProperty(value = "原始数据代码")
    private Long srcDataSetId;

    @ApiModelProperty(value = "目标数据代码")
    private Long tgtDataSetId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
