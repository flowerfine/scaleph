package com.liyu.breeze.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.UserDept;
import com.liyu.breeze.dao.mapper.UserDeptMapper;
import com.liyu.breeze.service.admin.UserDeptService;
import com.liyu.breeze.service.convert.admin.UserDeptConvert;
import com.liyu.breeze.service.dto.admin.UserDeptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户和部门关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class UserDeptServiceImpl implements UserDeptService {

    @Autowired
    private UserDeptMapper userDeptMapper;

    @Override
    public int insert(UserDeptDTO userDeptDTO) {
        UserDept userDept = UserDeptConvert.INSTANCE.toDo(userDeptDTO);
        return this.userDeptMapper.insert(userDept);
    }

    @Override
    public int deleteBydeptId(Serializable deptId) {
        return this.userDeptMapper.delete(new LambdaQueryWrapper<UserDept>()
                .eq(UserDept::getDeptId, deptId));
    }

    @Override
    public int delete(UserDeptDTO userDeptDTO) {
        return this.userDeptMapper.delete(new LambdaQueryWrapper<UserDept>()
                .eq(UserDept::getDeptId, userDeptDTO.getDeptId())
                .eq(UserDept::getUserId, userDeptDTO.getUserId())
        );
    }

    @Override
    public List<UserDeptDTO> listByDeptId(Serializable deptId) {
        List<UserDept> list = this.userDeptMapper.selectList(new LambdaQueryWrapper<UserDept>()
                .eq(UserDept::getDeptId, deptId));
        return UserDeptConvert.INSTANCE.toDto(list);
    }
}
