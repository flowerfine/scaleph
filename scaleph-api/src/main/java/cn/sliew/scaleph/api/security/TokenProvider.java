package cn.sliew.scaleph.api.security;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 使用jwt或者uuid等规则生成用户的token
 *
 * @author gleiyu
 */
@Slf4j
@Component
public class TokenProvider {
    /**
     * 使用uuid作为token
     */
    public String createToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
