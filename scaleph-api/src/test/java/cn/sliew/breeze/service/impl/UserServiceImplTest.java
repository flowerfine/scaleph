package cn.sliew.breeze.service.impl;

import cn.sliew.breeze.ApplicationTest;
import cn.sliew.breeze.common.enums.UserStatusEnum;
import cn.sliew.breeze.service.admin.UserService;
import cn.sliew.breeze.service.dto.admin.UserDTO;
import cn.sliew.breeze.service.vo.DictVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

class UserServiceImplTest extends ApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void simpleTest() {
        UserDTO user = new UserDTO();
        user.setUserName("test");
        user.setEmail("55@5");
        user.setPassword("123456");
        user.setUserStatus(new DictVO(UserStatusEnum.BIND_EMAIL.getValue(), UserStatusEnum.BIND_EMAIL.getLabel()));
        user.setRegisterChannel(new DictVO("15", "abc"));
        this.userService.insert(user);
        UserDTO dto = this.userService.selectOne("test");
        log.info(dto.getUserName());
    }

    @Test
    public void deleteByTypeTest() {
        this.userService.deleteBatch(new HashMap<Integer, Integer>() {{
            put(0, 1);
            put(1, 2);
        }});
    }

}
