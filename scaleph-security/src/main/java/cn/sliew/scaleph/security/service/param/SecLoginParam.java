package cn.sliew.scaleph.security.service.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Schema(name = "登录信息对象", description = "登录信息对象")
public class SecLoginParam {

    @NotBlank
    private String uuid;

    @NotBlank
    @Length(max = 5)
    private String authCode;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    private String userName;

    @NotBlank
    @Length(min = 6, max = 32)
    private String password;

    private Boolean remember;

    public Boolean getRemember() {
        return remember != null && remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
    }
}
