package cn.sliew.scaleph.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author gleiyu
 */
@Data
@AllArgsConstructor
@ApiModel(value = "TransferVO", description = "用于穿梭框数据交互")
public class TransferVO {
    private String value;
    private String name;
}
