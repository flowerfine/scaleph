package cn.sliew.scaleph.service.dto.di;

import cn.sliew.scaleph.service.dto.BaseDTO;
import cn.sliew.scaleph.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据集成-作业参数
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业参数信息", description = "数据集成-作业参数")
public class DiJobAttrDTO extends BaseDTO {

    private static final long serialVersionUID = -1088298944833438990L;

    @NotNull
    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @NotNull
    @ApiModelProperty(value = "作业参数类型")
    private DictVO jobAttrType;

    @NotBlank
    @Length(min = 1,max = 128)
    @ApiModelProperty(value = "作业参数key")
    private String jobAttrKey;

    @ApiModelProperty(value = "作业参数value")
    private String jobAttrValue;


}
