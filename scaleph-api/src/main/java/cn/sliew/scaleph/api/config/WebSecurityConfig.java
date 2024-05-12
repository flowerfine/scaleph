/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.api.config;

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.util.SpringApplicationContextUtil;
import cn.sliew.scaleph.security.authentication.CustomAccessDeniedHandler;
import cn.sliew.scaleph.security.authentication.CustomAuthenticationEntryPoint;
import cn.sliew.scaleph.security.authorization.CustomAuthorizationManager;
import cn.sliew.scaleph.security.authorization.CustomRequestMatcher;
import cn.sliew.scaleph.security.config.TokenConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession(redisNamespace = "${spring.application.name}")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private TokenConfigurer tokenConfigurer;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomRequestMatcher customRequestMatcher;
    @Autowired
    private CustomAuthorizationManager customAuthorizationManager;

    /**
     * BCryptPasswordEncoder 自带加盐功能。密钥迭代次数为 2^strength。strength 区间为 4~31，默认 10
     * 数据库存入的 sys_admin 用户密码即为使用这个加密的，对这里做任何调整，都要调整数据库中的密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        // 禁用cors
        http.csrf(this::csrf);
        // 禁用iframe
        http.headers(this::headers);
        // fixme 表单登陆不能用于前后端分离模式下的登陆
        // fixme 如果要实现前后端分离，使用 json 获取登陆信息，需要自定义拦截器
        http.formLogin(this::formLogin);
        // u_token 认证
        http.apply(tokenConfigurer);
        // 请求权限配置
        http.authorizeHttpRequests(this::authorizeRequests);
        // session
        http.sessionManagement(this::sessionManagement);
        http.exceptionHandling(this::exceptionHandling);
        // @formatter:on
        return http.build();
    }

    private void csrf(CsrfConfigurer<HttpSecurity> csrf) {
        csrf.disable();
    }

    private void headers(HeadersConfigurer<HttpSecurity> headers) {
        headers.frameOptions(frameOptions -> frameOptions.disable());
    }

    private void formLogin(FormLoginConfigurer<HttpSecurity> formLogin) {
        formLogin.disable();
    }

    /**
     * spring-security 按照从上往下顺序来匹配，一旦匹配成功则不在匹配
     */
    private void authorizeRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeHttpRequests) {
        ApplicationContext applicationContext = SpringApplicationContextUtil.getApplicationContext();
        //查找匿名标记的资源
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class)
                .getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (!ObjectUtils.isEmpty(anonymousAccess)) {
                anonymousUrls.addAll(requestMappingInfo.getPatternValues());
            }
        }

        //放行endpoint
        authorizeHttpRequests.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        //自定义匿名访问url
        authorizeHttpRequests.requestMatchers(anonymousUrls.toArray(new String[0])).permitAll();

        //静态资源
        authorizeHttpRequests.requestMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.png",
                "/**/*.woff", "/**/*.woff2", "/**/*.svg", "/**/*.json", "/**/*.ttf", "/**/*.ico",
                "/index.html").permitAll();
        authorizeHttpRequests.requestMatchers("/swagger**/**", "/doc.html", "/v3/**", "/webjars/**").permitAll();
        authorizeHttpRequests.requestMatchers("/ui/**").permitAll();
        //放行options请求
        authorizeHttpRequests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        authorizeHttpRequests.anyRequest().authenticated();
    }

    private void sessionManagement(SessionManagementConfigurer<HttpSecurity> sessionManagement) {
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void exceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) {
        exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
    }

    /**
     * fix When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header.
     * To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "responseType", Constants.TOKEN_KEY));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
