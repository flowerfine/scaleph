/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
