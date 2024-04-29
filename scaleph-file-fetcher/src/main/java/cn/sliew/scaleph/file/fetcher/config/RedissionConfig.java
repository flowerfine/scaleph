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

package cn.sliew.scaleph.file.fetcher.config;

import cn.sliew.scaleph.common.util.NetUtils;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(RedissonAutoConfiguration.class)
public class RedissionConfig implements RedissonAutoConfigurationCustomizer {

    @Override
    public void customize(Config config) {
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(NetUtils.replaceLocalhost(singleServerConfig.getAddress()));
    }
}
