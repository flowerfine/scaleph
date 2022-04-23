package com.liyu.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.UserActive;
import com.liyu.breeze.dao.mapper.UserActiveMapper;
import com.liyu.breeze.service.admin.UserActiveService;
import com.liyu.breeze.service.convert.admin.UserActiveConvert;
import com.liyu.breeze.service.dto.admin.UserActiveDTO;
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
