package cn.sliew.scaleph.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {
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
