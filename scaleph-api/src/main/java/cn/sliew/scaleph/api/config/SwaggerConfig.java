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

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.common.constant.Constants;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.List;

/**
 * swagger 配置。swagger 2 协议
 * 由于 springfox 更新跟不上 springboot 的迭代，springboot3 后，knife4j 只适配 openapi
 *
 * @author gleiyu
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket defaultApi() {
        Parameter tokenParam = new ParameterBuilder()
                .name(Constants.TOKEN_KEY)
                .description("user token")
                .parameterType("header")
                .required(true)
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("默认")
                .select()
//                .apis(RequestHandlerSelectors.basePackage("cn.sliew.scaleph"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(CollectionUtil.newArrayList(tokenParam));
//                .securityContexts(CollectionUtil.newArrayList(securityContext()))
//                .securitySchemes(CollectionUtil.newArrayList(apiKey()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Scaleph API文档")
                .description("Scaleph API文档")
                .contact(new Contact("gleiyu", "https://github.com/flowerfine/scaleph", "gleiyu@sina.cn"))
                .contact(new Contact("kalencaya", "https://github.com/flowerfine/scaleph", "1942460489@qq.com"))
                .version("1.0.2")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("BearerToken", Constants.TOKEN_KEY, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtil.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
}
