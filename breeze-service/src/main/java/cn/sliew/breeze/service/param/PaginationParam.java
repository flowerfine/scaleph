package cn.sliew.breeze.service.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gleiyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页参数")
public class PaginationParam implements Serializable {
    private static final long serialVersionUID = -860020632404225667L;

    @ApiModelProperty(value = "页码", example = "1")
    private Long current = 1L;

    @ApiModelProperty(value = "页面大小", example = "10")
    private Long pageSize = 10L;
}
