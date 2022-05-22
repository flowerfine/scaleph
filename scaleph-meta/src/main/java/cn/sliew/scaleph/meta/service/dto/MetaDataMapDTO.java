package cn.sliew.scaleph.meta.service.dto;

import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

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

    @ApiModelProperty(value = "源参考数据类型编码")
    private String srcDataSetTypeCode;

    @ApiModelProperty(value = "源参考数据类型名称")
    private String srcDataSetTypeName;

    @NotNull
    @ApiModelProperty(value = "源参考数据id")
    private Long srcDataSetId;

    @ApiModelProperty(value = "源参考数据编码")
    private String srcDataSetCode;

    @ApiModelProperty(value = "源参考数据值")
    private String srcDataSetValue;

    @ApiModelProperty(value = "目标参考数据类型编码")
    private String tgtDataSetTypeCode;

    @ApiModelProperty(value = "目标参考数据类型名称")
    private String tgtDataSetTypeName;

    @NotNull
    @ApiModelProperty(value = "目标参考数据id")
    private Long tgtDataSetId;

    @ApiModelProperty(value = "目标参考数据编码")
    private String tgtDataSetCode;

    @ApiModelProperty(value = "目标参考数据值")
    private String tgtDataSetValue;

    @ApiModelProperty(value = "备注")
    private String remark;


}
