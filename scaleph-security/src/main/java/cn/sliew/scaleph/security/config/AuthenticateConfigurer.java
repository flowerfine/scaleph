package cn.sliew.scaleph.security.config;

import cn.sliew.scaleph.security.authentication.CustomUsernamePasswordAuthenticateFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

/**
 * 注册Filter：
 * 通过 {@link org.springframework.security.config.annotation.web.builders.HttpSecurity#addFilterAt(Filter, Class)} 注册
 * 通过 继承 {@link org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer}
 * <p>
 * 这里通过继承 {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter} 的方式实现登陆逻辑，
 * 因此通过 AbstractHttpConfigurer 注册。
 */
public class AuthenticateConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, AuthenticateConfigurer<H>, CustomUsernamePasswordAuthenticateFilter> {

    public AuthenticateConfigurer() {
        super(new CustomUsernamePasswordAuthenticateFilter(), null);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }
}
