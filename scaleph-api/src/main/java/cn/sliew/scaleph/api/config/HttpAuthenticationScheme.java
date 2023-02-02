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

import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.VendorExtension;

import java.util.List;

/**
 * @since:knife4j-spring-boot27-demo
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/21 15:45
 */
public class HttpAuthenticationScheme extends SecurityScheme {
    public static final HttpAuthenticationBuilder BASIC_AUTH_BUILDER = new HttpAuthenticationBuilder().scheme("basic");
    public static final HttpAuthenticationBuilder JWT_BEARER_BUILDER = new HttpAuthenticationBuilder()
            .scheme("bearer")
            .bearerFormat("JWT");

    public HttpAuthenticationScheme(
            String name,
            String description,
            String type,
            String scheme,
            String bearerFormat,
            List<VendorExtension> extensions) {
        super(name, type);
    }
    protected HttpAuthenticationScheme(String name, String type) {
        super(name, type);
    }
}