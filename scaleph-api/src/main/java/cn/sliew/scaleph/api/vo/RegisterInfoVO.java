package cn.sliew.scaleph.api.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "用户注册信息", description = "用户注册信息")
public class RegisterInfoVO {
    private static final long serialVersionUID = -2183662104593082350L;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotBlank
    @Email
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @Length(min = 6, max = 32)
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank
    @Length(min = 6, max = 32)
    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;

    @NotBlank
    @Length(max = 5)
    @ApiModelProperty(value = "验证码")
    private String authCode;

    @NotBlank
    @ApiModelProperty(value = "验证码uuid")
    private String uuid;

}
