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

package cn.sliew.scaleph.cache;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author gleiyu
 */
@Configuration
public class CaffeineCacheConfig {

    public static final int INITIAL_CAPACITY = 100;
    public static final int MAX_CAPACITY = 10000;

    /**
     * 无界缓存manager，只初始化大小，缓存清理需手动处理，没有缓存过期策略
     *
     * @return CacheManager
     */
    @Bean("unBoundedCacheManager")
    public CacheManager unBoundedCaffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(
            Caffeine.newBuilder().initialCapacity(CaffeineCacheConfig.INITIAL_CAPACITY));
        caffeineCacheManager.setAllowNullValues(false);
        return caffeineCacheManager;
    }

    /**
     * 有界缓存
     *
     * @return CacheManager
     */
    @Primary
    @Bean("boundedCacheManager")
    public CacheManager boundedCaffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
            .initialCapacity(CaffeineCacheConfig.INITIAL_CAPACITY)
            .maximumSize(CaffeineCacheConfig.MAX_CAPACITY)
            .expireAfterWrite(60, TimeUnit.SECONDS)
        );
        caffeineCacheManager.setAllowNullValues(false);
        return caffeineCacheManager;
    }

}
