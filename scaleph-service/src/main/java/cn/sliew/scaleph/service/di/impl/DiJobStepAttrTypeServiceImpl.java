package cn.sliew.scaleph.service.di.impl;

import cn.sliew.scaleph.dao.entity.master.di.DiJobStepAttrType;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobStepAttrTypeMapper;
import cn.sliew.scaleph.service.config.CaffeineCacheConfig;
import cn.sliew.scaleph.service.convert.di.DiJobStepAttrTypeConvert;
import cn.sliew.scaleph.service.di.DiJobStepAttrTypeService;
import cn.sliew.scaleph.service.dto.di.DiJobStepAttrTypeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
