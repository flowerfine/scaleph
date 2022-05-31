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
