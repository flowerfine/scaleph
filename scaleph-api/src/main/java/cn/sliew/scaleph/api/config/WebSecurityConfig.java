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
import cn.sliew.scaleph.security.web.CustomAccessDeniedHandler;
import cn.sliew.scaleph.security.web.CustomAuthenticationEntryPoint;
import cn.sliew.scaleph.security.web.TokenConfigurer;
import cn.sliew.scaleph.system.util.SpringApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private TokenConfigurer tokenConfigurer;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                anonymousUrls.addAll(requestMappingInfo.getPathPatternsCondition().getPatternValues());
            }
        }

        http
                //禁用cors
                .csrf().disable()
                //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //禁用iframe
                .and()
                .headers()
                .frameOptions()
                .disable()

                //请求权限配置
                .and()
                .authorizeRequests()
                //自定义匿名访问url
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                //静态资源
                .antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.png",
                        "/**/*.woff", "/**/*.woff2", "/**/*.svg", "/**/*.json", "/**/*.ttf", "/**/*.ico",
                        "/index.html"
                ).permitAll()
                .antMatchers("/swagger**/**", "/doc.html", "/v3/**", "/webjars/**").permitAll()
                //放行options请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                //禁用session
                .and()
                .apply(tokenConfigurer)
        ;
        return http.build();
    }
}
