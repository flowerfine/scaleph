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

package cn.sliew.scaleph.system.cache;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import cn.hutool.core.util.ObjectUtil;
import cn.sliew.scaleph.cache.CachingConfig;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author gleiyu
 */
@Slf4j
@Component
public class DictTypeCache {

    private static Cache dictTypeCache;

    @Autowired
    @Qualifier(value = "unBoundedCacheManager")
    private CacheManager cacheManager;

    @Autowired
    private SysDictTypeService sysDictTypeService;

    public DictTypeCache() {

    }

    public synchronized static void updateCache(List<SysDictTypeDTO> list) {
        for (SysDictTypeDTO dictType : list) {
            updateCache(dictType);
        }
    }

    public synchronized static void updateCache(SysDictTypeDTO dictType) {
        if (ObjectUtil.isNotNull(dictType)) {
            dictTypeCache.put(dictType.getDictTypeCode(), dictType.getDictTypeName());
        }
    }

    public synchronized static void evictCache(String key) {
        dictTypeCache.evict(key);
    }

    public static String getValueByKey(String key) {
        return dictTypeCache.get(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public static List<SysDictTypeDTO> listAll() {
        List<SysDictTypeDTO> list = new ArrayList<>();
        com.github.benmanes.caffeine.cache.Cache<String, String> localCache =
            (com.github.benmanes.caffeine.cache.Cache<String, String>) dictTypeCache.getNativeCache();
        ConcurrentMap<String, String> map = localCache.asMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            SysDictTypeDTO dictType = new SysDictTypeDTO();
            dictType.setDictTypeCode(entry.getKey());
            dictType.setDictTypeName(entry.getValue());
            list.add(dictType);
        }
        return list;
    }

    @PostConstruct
    public synchronized void init() {
        log.info("initializing cache " + CachingConfig.UnBoundedCaches.CACHE_DICT_TYPE);
        dictTypeCache = cacheManager.getCache(CachingConfig.UnBoundedCaches.CACHE_DICT_TYPE);
        List<SysDictTypeDTO> list = this.sysDictTypeService.selectAll();
        dictTypeCache.clear();
        updateCache(list);
    }
}
