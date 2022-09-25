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

package cn.sliew.scaleph.core.di.service.impl;

import java.util.List;

import cn.sliew.scaleph.cache.CachingConfig;
import cn.sliew.scaleph.core.di.service.DiJobStepAttrTypeService;
import cn.sliew.scaleph.core.di.service.convert.DiJobStepAttrTypeConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepAttrTypeDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobStepAttrType;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobStepAttrTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author gleiyu
 *
 * todo remove this class
 */
@Service
@CacheConfig(cacheNames = CachingConfig.UnBoundedCaches.CACHE_JOB_STEP_ATTR_TYPE, cacheManager = "unBoundedCacheManager")
public class DiJobStepAttrTypeServiceImpl implements DiJobStepAttrTypeService {

    @Autowired
    private DiJobStepAttrTypeMapper diJobStepAttrTypeMapper;

    @Override
    @Cacheable(key = "#stepType+'-'+#stepName")
    public List<DiJobStepAttrTypeDTO> listByType(String stepType, String stepName) {
        List<DiJobStepAttrType> list = this.diJobStepAttrTypeMapper.selectList(
            new LambdaQueryWrapper<DiJobStepAttrType>()
                .eq(DiJobStepAttrType::getStepType, stepType)
                .eq(DiJobStepAttrType::getStepName, stepName)
        );
        return DiJobStepAttrTypeConvert.INSTANCE.toDto(list);
    }
}
