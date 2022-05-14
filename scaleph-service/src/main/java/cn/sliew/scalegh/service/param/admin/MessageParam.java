package cn.sliew.scalegh.service.param.admin;

import cn.sliew.scalegh.service.param.PaginationParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageParam extends PaginationParam {

    @ApiModelProperty(value = "收件人")
    private String receiver;

    @ApiModelProperty(value = "发件人")
    private String sender;

    @ApiModelProperty(value = "消息类型")
    private String messageType;

    @ApiModelProperty(value = "是否读取")
    private String isRead;
}
