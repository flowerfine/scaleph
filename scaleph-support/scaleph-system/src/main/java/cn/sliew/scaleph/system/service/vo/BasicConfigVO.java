package cn.sliew.scaleph.system.service.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "基础配置信息", description = "基础配置信息")
public class BasicConfigVO {
    @NotNull
    private String seatunnelHome;
}
