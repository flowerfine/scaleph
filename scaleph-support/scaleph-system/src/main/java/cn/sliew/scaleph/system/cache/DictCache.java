package cn.sliew.scaleph.system.cache;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import cn.sliew.scaleph.cache.CachingConfig;
import cn.sliew.scaleph.common.enums.BoolEnum;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
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
public class DictCache {
    private static Cache dictCache;

    @Autowired
    @Qualifier(value = "unBoundedCacheManager")
    private CacheManager cacheManager;

    @Autowired
    private SysDictService sysDictService;

    public DictCache() {

    }

    public synchronized static void updateCache(List<SysDictDTO> list) {
        for (SysDictDTO dictType : list) {
            updateCache(dictType);
        }
    }

    public synchronized static void updateCache(SysDictDTO dict) {
        if (ObjectUtil.isNotNull(dict.getDictType())) {
            if (BoolEnum.YES.getValue().equals(dict.getIsValid())) {
                dictCache.put(dict.getKey(), dict.getDictValue());
            } else {
                evictCache(dict);
            }
        }
    }

    public synchronized static void evictCache(SysDictDTO dict) {
        evictCache(dict.getKey());
    }

    public synchronized static void evictCache(String key) {
        dictCache.evict(key);
    }

    @SuppressWarnings("unchecked")
    public synchronized static void evictCacheByType(String dictTypeCode) {
        com.github.benmanes.caffeine.cache.Cache<String, String> cache =
            (com.github.benmanes.caffeine.cache.Cache<String, String>) dictCache.getNativeCache();
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
    public static List<SysDictDTO> getDictByType(String dictTypeCode) {
        List<SysDictDTO> list = new ArrayList<>();
        com.github.benmanes.caffeine.cache.Cache<String, String> cache =
            (com.github.benmanes.caffeine.cache.Cache<String, String>) dictCache.getNativeCache();
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            String key = entry.getKey();
            String typeCode = key.split("-")[0];
            String dictCode = key.split("-")[1];
            String dictValue = entry.getValue();
            if (typeCode.equals(dictTypeCode)) {
                SysDictDTO sysDictDTO = new SysDictDTO();
                sysDictDTO.setDictCode(dictCode);
                sysDictDTO.setDictValue(dictValue);
                SysDictTypeDTO type = new SysDictTypeDTO();
                type.setDictTypeCode(typeCode);
                list.add(sysDictDTO);
            }
        }
        return list;
    }

    @PostConstruct
    public synchronized void init() {
        log.info("initializing cache " + CachingConfig.UnBoundedCaches.CACHE_DICT);
        dictCache = cacheManager.getCache(CachingConfig.UnBoundedCaches.CACHE_DICT);
        List<SysDictDTO> list = this.sysDictService.selectAll();
        dictCache.clear();
        updateCache(list);
    }

}
