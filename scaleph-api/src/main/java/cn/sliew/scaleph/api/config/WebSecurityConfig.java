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
import cn.sliew.scaleph.security.config.AuthenticateConfigurer;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
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
@EnableWebSecurity(debug = false)
@EnableRedisHttpSession(redisNamespace = "${spring.application.name}")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private TokenConfigurer tokenConfigurer;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * BCryptPasswordEncoder 自带加盐功能。密钥迭代次数为 2^strength。strength 区间为 4~31，默认 10
     * 这里随便改个默认值
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(23);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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

        // @formatter:off
        http
                //禁用cors
                .csrf().disable()

                //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                //禁用iframe
                .headers()
                    .frameOptions().disable()
                .and()

                //fixme 表单登陆不能用于前后端分离模式下的登陆
                //fixme 如果要实现前后端分离，使用 json 获取登陆信息，需要自定义拦截器
                .formLogin().disable()
//                    .apply(new AuthenticateConfigurer<>())
//                    .loginProcessingUrl("/api/user/login") // 服务端登陆接口
//                .usernameParameter("userName")
//                .passwordParameter("password")
//                .successHandler(null)
//                .failureHandler(null)
//                    .permitAll()
//                .and()

//                .rememberMe()
                // 用于散列的值，随便填写即可
//                .key("rememberMe")
                // remember-me 将信息存入 cookie，如果用户拿到 cookie 里面的信息，则可以直接绕过登陆
                // PersistentTokenRepository 记录 remember-me cookie 生成时的地址，防止攻击者使用用户 cookie 绕过登陆
//                .tokenRepository(new JdbcTokenRepositoryImpl())
//                .tokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(16L))
//                .and()

                // todo 注销
//                .logout()
//                .logoutUrl("/logout")
//                .deleteCookies()
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .permitAll()
//                .and()

                //请求权限配置
                // spring-security 按照从上往下顺序来匹配，一旦匹配成功则不在匹配
                .authorizeRequests()
                    //放行endpoint
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                    //自定义匿名访问url
                    .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                    //静态资源
                    .antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.png",
                        "/**/*.woff", "/**/*.woff2", "/**/*.svg", "/**/*.json", "/**/*.ttf", "/**/*.ico",
                        "/index.html").permitAll()
                    .antMatchers("/swagger**/**", "/doc.html", "/v3/**", "/webjars/**").permitAll()
                    .antMatchers("/ui/**").permitAll()
                    //放行options请求
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated()
                .and()

                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
                .and()

                // session
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    // 同一个用户最多有 1 个 session，可达成后面登陆会自动踢掉前面的登陆
//                    .maximumSessions(1)
//                    // 在最多有 1 个 session 存在的限制下，默认的是后面登陆会自动踢掉前面的登陆
//                    // 如果要达成已经登陆后，后面无法登陆的效果，则通过如下配置即可
//                    // 加上这个限制后，需设置 HttpSessionEventPublisher 监听 session 时间，
//                    // 发布 session 的创建、销毁时间，触发 spring-security 内部的机制
//                    .maxSessionsPreventsLogin(true)
//                    .and()
//                    // session 固定攻击
//                    .sessionFixation().migrateSession()
//                .and()

                // u_token
                .apply(tokenConfigurer)
        ;
        // @formatter:on
        return http.build();
    }

    /**
     * fix When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header.
     * To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead
     */
//    @Bean
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
