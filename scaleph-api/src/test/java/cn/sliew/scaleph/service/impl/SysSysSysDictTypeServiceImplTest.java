package cn.sliew.scaleph.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.system.service.SysDictTypeService;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SysSysSysDictTypeServiceImplTest extends ApplicationTest {

    @Autowired
    private SysDictTypeService sysDictTypeService;

    @Test
    public void simpleTest() {
        SysDictTypeDTO dto = new SysDictTypeDTO();
        dto.setDictTypeCode("test_code");
        dto.setDictTypeName("name");
        dto.setRemark("备注");
        dto.setCreator("test");
        dto.setEditor("test");
        dto.setUpdateTime(new Date());
        dto.setCreateTime(new Date());
        this.sysDictTypeService.insert(dto);
        SysDictTypeDTO dto2 = this.sysDictTypeService.selectOne("test_code");
        dto2.setRemark("修改");
        this.sysDictTypeService.update(dto2);
        this.sysDictTypeService.deleteById(dto2.getId());
    }

    @Test
    public void deleteBatchTest() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 11);
        map.put(2, 12);
        map.put(3, 13);
        this.sysDictTypeService.deleteBatch(map);
    }

    @Test
    public void listByPageTest() {
//        DictTypeParam param = new DictTypeParam();
//        param.setPageNum(null);
//        param.setPageSize(null);
//        param.setDictTypeCode(null);
        List<SysDictTypeDTO> list = this.sysDictTypeService.selectAll();
        for (SysDictTypeDTO dto : list) {
            log.info(dto.getDictTypeCode());
        }
    }
}
