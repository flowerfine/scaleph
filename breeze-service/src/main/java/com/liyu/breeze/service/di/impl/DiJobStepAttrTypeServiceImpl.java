package com.liyu.breeze.service.di.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.DiJobStepAttrType;
import com.liyu.breeze.dao.mapper.DiJobStepAttrTypeMapper;
import com.liyu.breeze.service.di.DiJobStepAttrTypeService;
import com.liyu.breeze.service.config.CaffeineCacheConfig;
import com.liyu.breeze.service.convert.di.DiJobStepAttrTypeConvert;
import com.liyu.breeze.service.dto.di.DiJobStepAttrTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gleiyu
 */
@Service
@CacheConfig(cacheNames = CaffeineCacheConfig.UnBoundedCaches.CACHE_JOB_STEP_ATTR_TYPE, cacheManager = "unBoundedCacheManager")
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
