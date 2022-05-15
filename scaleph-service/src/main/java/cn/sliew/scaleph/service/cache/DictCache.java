package cn.sliew.scaleph.service.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.sliew.scaleph.common.enums.BoolEnum;
import cn.sliew.scaleph.service.admin.DictService;
import cn.sliew.scaleph.service.config.CaffeineCacheConfig;
import cn.sliew.scaleph.service.dto.admin.DictDTO;
import cn.sliew.scaleph.service.dto.admin.DictTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@Slf4j
@Component
public class DictCache {
    private static Cache dictCache;

    @Autowired
    @Qualifier(value = "unBoundedCacheManager")
    private CacheManager cacheManager;

    @Autowired
    private DictService dictService;

    public DictCache() {

    }

    @PostConstruct
    public synchronized void init() {
        log.info("initializing cache " + CaffeineCacheConfig.UnBoundedCaches.CACHE_DICT);
        dictCache = cacheManager.getCache(CaffeineCacheConfig.UnBoundedCaches.CACHE_DICT);
        List<DictDTO> list = this.dictService.selectAll();
        dictCache.clear();
        updateCache(list);
    }

    public synchronized static void updateCache(List<DictDTO> list) {
        for (DictDTO dictType : list) {
            updateCache(dictType);
        }
    }

    public synchronized static void updateCache(DictDTO dict) {
        if (ObjectUtil.isNotNull(dict.getDictType())) {
            if (BoolEnum.YES.getValue().equals(dict.getIsValid())) {
                dictCache.put(dict.getKey(), dict.getDictValue());
            } else {
                evictCache(dict);
            }
        }
    }

    public synchronized static void evictCache(DictDTO dict) {
        evictCache(dict.getKey());
    }

    public synchronized static void evictCache(String key) {
        dictCache.evict(key);
    }

    @SuppressWarnings("unchecked")
    public synchronized static void evictCacheByType(String dictTypeCode) {
        com.github.benmanes.caffeine.cache.Cache<String, String> cache = (com.github.benmanes.caffeine.cache.Cache<String, String>) dictCache.getNativeCache();
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            String key = entry.getKey();
            String keyPrefix = entry.getKey().split("-")[0];
            if (dictTypeCode.equals(keyPrefix)) {
                evictCache(key);
            }
        }
    }

    public static String getValueByKey(String key) {
        return dictCache.get(key, String.class);
    }

    /**
     * 按类型返回数据字典
     *
     * @param dictTypeCode 字典编码
     * @return List<DictDTO>
     */
    @SuppressWarnings("unchecked")
    public static List<DictDTO> getDictByType(String dictTypeCode) {
        List<DictDTO> list = new ArrayList<>();
        com.github.benmanes.caffeine.cache.Cache<String, String> cache = (com.github.benmanes.caffeine.cache.Cache<String, String>) dictCache.getNativeCache();
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            String key = entry.getKey();
            String typeCode = key.split("-")[0];
            String dictCode = key.split("-")[1];
            String dictValue = entry.getValue();
            if (typeCode.equals(dictTypeCode)) {
                DictDTO dictDTO = new DictDTO();
                dictDTO.setDictCode(dictCode);
                dictDTO.setDictValue(dictValue);
                DictTypeDTO type = new DictTypeDTO();
                type.setDictTypeCode(typeCode);
                list.add(dictDTO);
            }
        }
        return list;
    }

}
