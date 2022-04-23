package com.liyu.breeze.service.impl;

import com.liyu.breeze.ApplicationTest;
import com.liyu.breeze.service.admin.DictTypeService;
import com.liyu.breeze.service.dto.admin.DictTypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DictTypeServiceImplTest extends ApplicationTest {

    @Autowired
    private DictTypeService dictTypeService;

    @Test
    public void simpleTest() {
        DictTypeDTO dto = new DictTypeDTO();
        dto.setDictTypeCode("test_code");
        dto.setDictTypeName("name");
        dto.setRemark("备注");
        dto.setCreator("test");
        dto.setEditor("test");
        dto.setUpdateTime(new Date());
        dto.setCreateTime(new Date());
        this.dictTypeService.insert(dto);
        DictTypeDTO dto2 = this.dictTypeService.selectOne("test_code");
        dto2.setRemark("修改");
        this.dictTypeService.update(dto2);
        this.dictTypeService.deleteById(dto2.getId());
    }

    @Test
    public void deleteBatchTest() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 11);
        map.put(2, 12);
        map.put(3, 13);
        this.dictTypeService.deleteBatch(map);
    }

    @Test
    public void listByPageTest() {
//        DictTypeParam param = new DictTypeParam();
//        param.setPageNum(null);
//        param.setPageSize(null);
//        param.setDictTypeCode(null);
        List<DictTypeDTO> list = this.dictTypeService.selectAll();
        for (DictTypeDTO dto : list) {
            log.info(dto.getDictTypeCode());
        }
    }
}
