package cn.sliew.scaleph.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 配置token过滤器在用户密码过滤器之前
 *
 * @author gleiyu
 */
@Component
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenFilter tokenFilter;

    @Autowired
    public void setTokenFilter(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
