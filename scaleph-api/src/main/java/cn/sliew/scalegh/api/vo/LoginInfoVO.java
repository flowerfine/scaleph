package cn.sliew.scalegh.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "登录信息对象", description = "登录信息对象")
public class LoginInfoVO {
    private static final long serialVersionUID = 2126239295368790260L;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    private String userName;

    @NotBlank
    @Length(min = 6, max = 32)
    private String password;
    /**
     * 验证码信息
     */
    @NotBlank
    @Length(max = 5)
    private String authCode;

    @NotBlank
    private String uuid;

    private Boolean remember;

    public Boolean getRemember() {
        return remember != null && remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
    }
}
