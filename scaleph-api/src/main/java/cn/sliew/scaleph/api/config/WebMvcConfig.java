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

import cn.sliew.scaleph.api.util.RequestParamUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.util.FieldUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    @Autowired
    private ObjectMapper mapper;

    /**
     * with purpose for supporting enum on springmvc pathvariable, jackson, mybatis-plus
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addConverter(new JacksonEnumConverter());
    }



    /**
     * 通用拦截器排除swagger设置，所有拦截器都会自动加swagger相关的资源排除信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            registry.addInterceptor(new AsyncWebLogInterceptor());
            Field registrationsField = FieldUtils.getField(InterceptorRegistry.class, "registrations");
            ReflectionUtils.makeAccessible(registrationsField);
            List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) ReflectionUtils.getField(registrationsField, registry);
            if (registrations != null) {
                for (InterceptorRegistration interceptorRegistration : registrations) {
                    interceptorRegistration
                            .excludePathPatterns("/favicon.ico")
                            .excludePathPatterns("/swagger**/**")
                            .excludePathPatterns("/webjars/**")
                            .excludePathPatterns("/v3/api-docs/**")
                            .excludePathPatterns("/doc.html");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 这玩意会造成异步请求结果无法响应，换成{@code AsyncHandlerInterceptor}就一切正常了
     */
    @Slf4j
    @Component
    public static class WebLogInterceptor extends OncePerRequestFilter implements Ordered {

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE - 10;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if (!RequestParamUtil.isRequestValid(request)
                    || RequestParamUtil.ignorePath(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
            if (!(request instanceof ContentCachingRequestWrapper)) {
                request = new ContentCachingRequestWrapper(request);
            }
            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
            }

            filterChain.doFilter(request, response);
        }
    }

    @Slf4j
    public static class AsyncWebLogInterceptor implements AsyncHandlerInterceptor {

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            logQuery(request, response);
        }

        private void logQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
            if (!Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())
                    && log.isInfoEnabled()) {
                String method = request.getMethod();
                String uri = request.getRequestURI();
                String parameters = RequestParamUtil.getRequestParams(request);
                String body = RequestParamUtil.getRequestBody(request);
                log.info("{} {} 请求参数 parameters: [{}], body: [{}]", method, uri, parameters, body);
                ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
                if (responseWrapper != null) {
                    responseWrapper.copyBodyToResponse();
                }
            }
        }
    }

    private class JacksonEnumConverter implements GenericConverter {

        private Set<ConvertiblePair> set;

        public JacksonEnumConverter() {
            set = new HashSet<>();
            set.add(new ConvertiblePair(String.class, Enum.class));
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return set;
        }

        @Override
        public Object convert(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (value == null) {
                return null;
            }
            try {
                return mapper.readValue("\"" + value + "\"", targetType.getType());
            } catch (IOException e) {
                throw new ConversionFailedException(sourceType, targetType, value, e);
            }
        }
    }
}
