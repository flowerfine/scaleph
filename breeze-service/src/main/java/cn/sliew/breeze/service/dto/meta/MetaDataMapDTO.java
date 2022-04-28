package cn.sliew.breeze.service.dto.meta;

import cn.sliew.breeze.service.dto.BaseDTO;
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
@ApiModel(value = "MetaDataMap对象", description = "元数据-参考数据映射")
public class MetaDataMapDTO extends BaseDTO {

    private static final long serialVersionUID = 866554862011424582L;

    @ApiModelProperty(value = "原始数据代码")
    private Long srcDataSetId;

    @ApiModelProperty(value = "目标数据代码")
    private Long tgtDataSetId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
