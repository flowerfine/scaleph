package cn.sliew.scalegh.service.impl;

import cn.sliew.scalegh.ApplicationTest;
import cn.sliew.scalegh.service.admin.DictService;
import cn.sliew.scalegh.service.dto.admin.DictDTO;
import cn.sliew.scalegh.service.dto.admin.DictTypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;

import java.util.List;

class DictServiceImplTest extends ApplicationTest {

    @Autowired
    private DictService dictService;

    @Autowired
    @Qualifier(value = "unBoundedCacheManager")
    private CacheManager cacheManager;

    @Test
    void simpleTest() {
        DictDTO dto = new DictDTO();

        DictTypeDTO typeDTO = new DictTypeDTO();
        typeDTO.setDictTypeCode("2");
        typeDTO.setDictTypeName("name");
        dto.setDictType(typeDTO);
        dto.setDictCode("hello3");
        dto.setDictValue("world");
        this.dictService.insert(dto);
        List<DictDTO> list = this.dictService.selectByType("2");
        for (DictDTO d : list) {
            log.info(d.getDictValue());
        }
    }

    @Test
    void deleteByTypeTest() {
        this.dictService.deleteByType("3");
    }

}
