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

import java.util.HashMap;

import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.common.enums.UserStatusEnum;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SecSecUserServiceImplTest extends ApplicationTest {

    @Autowired
    private SecUserService secUserService;

    @Test
    public void simpleTest() {
        SecUserDTO user = new SecUserDTO();
        user.setUserName("test");
        user.setEmail("55@5");
        user.setPassword("123456");
        user.setUserStatus(
            new DictVO(UserStatusEnum.BIND_EMAIL.getValue(), UserStatusEnum.BIND_EMAIL.getLabel()));
        user.setRegisterChannel(new DictVO("15", "abc"));
        this.secUserService.insert(user);
        SecUserDTO dto = this.secUserService.selectOne("test");
        log.info(dto.getUserName());
    }

    @Test
    public void deleteByTypeTest() {
        this.secUserService.deleteBatch(new HashMap<Integer, Integer>() {{
            put(0, 1);
            put(1, 2);
        }});
    }

}
