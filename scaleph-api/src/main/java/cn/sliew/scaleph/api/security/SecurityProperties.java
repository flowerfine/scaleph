package cn.sliew.scaleph.api.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "token")
public class SecurityProperties {
    /**
     * 令牌过期时间，单位毫秒
     */
    private Long tokenValidityInSeconds;

    /**
     * 长令牌过期时间，单位毫秒
     * 记住我功能
     */
    private Long longTokenValidityInSeconds;
}
