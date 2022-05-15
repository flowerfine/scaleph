package cn.sliew.scaleph.service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author gleiyu
 */
@EnableCaching
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
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().initialCapacity(CaffeineCacheConfig.INITIAL_CAPACITY));
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

    public interface UnBoundedCaches {
        String CACHE_DICT = "dict";
        String CACHE_DICT_TYPE = "dictType";
        String CACHE_ROLE = "role";
        String CACHE_JOB_STEP_ATTR_TYPE = "jobStepAttrType";
    }

    public interface BoundedCaches {
        String CACHE_DATA_SET = "dataSet";
    }
}
