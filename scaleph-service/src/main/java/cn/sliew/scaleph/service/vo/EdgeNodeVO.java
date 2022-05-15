package cn.sliew.scaleph.service.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 作业连线信息
 *
 * @author gleiyu
 */
@Data
@ApiModel(value = "作业edge对象", description = "作业连线信息")
public class EdgeNodeVO {
    @ApiModelProperty(value = "步骤id")
    private String cell;
    @ApiModelProperty(value = "连接桩")
    private String port;
}
