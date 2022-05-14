package cn.sliew.scalegh.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.dao.entity.master.system.UserActive;
import cn.sliew.scalegh.dao.mapper.master.system.UserActiveMapper;
import cn.sliew.scalegh.service.admin.UserActiveService;
import cn.sliew.scalegh.service.convert.admin.UserActiveConvert;
import cn.sliew.scalegh.service.dto.admin.UserActiveDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户邮箱激活日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
@Service
public class UserActiveServiceImpl implements UserActiveService {

    @Autowired
    private UserActiveMapper userActiveMapper;

    @Override
    public int insert(UserActiveDTO userActiveDTO) {
        UserActive userActive = UserActiveConvert.INSTANCE.toDo(userActiveDTO);
        return this.userActiveMapper.insert(userActive);
    }

    @Override
    public int updateByUserAndCode(UserActiveDTO userActiveDTO) {
        if (userActiveDTO != null && StrUtil.isEmpty(userActiveDTO.getUserName()) && StrUtil.isEmpty(userActiveDTO.getActiveCode())) {
            userActiveDTO.setActiveTime(new Date());
            UserActive userActive = UserActiveConvert.INSTANCE.toDo(userActiveDTO);
            return this.userActiveMapper.update(userActive, new LambdaQueryWrapper<UserActive>()
                    .eq(UserActive::getActiveCode, userActive.getActiveCode())
                    .eq(UserActive::getUserName, userActive.getUserName())
            );
        }
        return 0;
    }

    @Override
    public UserActiveDTO selectOne(String userName, String activeCode) {
        UserActive userActive = this.userActiveMapper.selectOne(new LambdaQueryWrapper<UserActive>()
                .eq(UserActive::getUserName, userName)
                .eq(UserActive::getActiveCode, activeCode)
        );
        return UserActiveConvert.INSTANCE.toDto(userActive);
    }
}
