package cn.sliew.scaleph.service.impl;

import java.util.List;

import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.system.service.SysDictService;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;

class SysSysDictServiceImplTest extends ApplicationTest {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    @Qualifier(value = "unBoundedCacheManager")
    private CacheManager cacheManager;

    @Test
    void simpleTest() {
        SysDictDTO dto = new SysDictDTO();

        SysDictTypeDTO typeDTO = new SysDictTypeDTO();
        typeDTO.setDictTypeCode("2");
        typeDTO.setDictTypeName("name");
        dto.setDictType(typeDTO);
        dto.setDictCode("hello3");
        dto.setDictValue("world");
        this.sysDictService.insert(dto);
        List<SysDictDTO> list = this.sysDictService.selectByType("2");
        for (SysDictDTO d : list) {
            log.info(d.getDictValue());
        }
    }

    @Test
    void deleteByTypeTest() {
        this.sysDictService.deleteByType("3");
    }

}
