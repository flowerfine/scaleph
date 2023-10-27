package cn.sliew.scaleph.security.service.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "验证码参数", description = "验证码参数")
public class SecCaptchaVerifyParam {

    @NotBlank
    @Schema(description = "验证码生成时的 uuid")
    private String uuid;

    @NotBlank
    @Length(max = 5)
    @Schema(description = "验证码")
    private String authCode;
}
