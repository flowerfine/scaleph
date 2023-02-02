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

import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;

/**
 * @since:knife4j-spring-boot27-demo
 * @auth <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2022/8/21 15:46
 */
public class HttpAuthenticationBuilder {
    private String name;
    private String description;
    private String scheme;
    private String bearerFormat;
    private final List<VendorExtension> extensions = new ArrayList<>();

    public HttpAuthenticationBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HttpAuthenticationBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public HttpAuthenticationBuilder bearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
        return this;
    }

    public HttpAuthenticationBuilder description(String description) {
        this.description = description;
        return this;
    }

    public HttpAuthenticationBuilder extensions(List<VendorExtension> extensions) {
        this.extensions.addAll(nullToEmptyList(extensions));
        return this;
    }

    public HttpAuthenticationScheme build() {
        return new HttpAuthenticationScheme(
                name,
                description,
                "http",
                scheme,
                bearerFormat,
                extensions);
    }
}