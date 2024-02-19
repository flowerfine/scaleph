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

package cn.sliew.scaleph.engine.flink.client.feign;

import feign.QueryMapEncoder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableFeignClients(basePackages = {"cn.sliew.scaleph.engine.flink.client.v1.api"})
public class DefaultFeignConfig {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonConverter;

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public QueryMapEncoder queryMapEncoder() {
        return new JacksonQueryMapEncoder();
    }

    @Bean
    public Encoder encoder() {
        addAdditionalMediaType();
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new SpringEncoder(objectFactory);
    }

    @Bean
    public Decoder decoder() {
        addAdditionalMediaType();
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    private void addAdditionalMediaType() {
        List<MediaType> supportedMediaTypes = new LinkedList<>(jacksonConverter.getSupportedMediaTypes());
        supportedMediaTypes.addAll(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
    }
}