package cn.sliew.scaleph.service.vo;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.constant.Constants;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统邮箱配置类
 *
 * @author gleiyu
 */
@Data
@ApiModel(value = "邮箱配置信息", description = "邮箱配置信息")
public class EmailConfigVO {
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String host;
    @NotNull
    private Integer port;
    private String encoding;

    public String getEncoding() {
        return StrUtil.isEmpty(this.encoding) ? Constants.DEFAULT_CHARSET : this.encoding;
    }


}
