package com.liyu.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.Privilege;
import com.liyu.breeze.dao.mapper.PrivilegeMapper;
import com.liyu.breeze.service.admin.PrivilegeService;
import com.liyu.breeze.service.convert.admin.PrivilegeConvert;
import com.liyu.breeze.service.dto.admin.PrivilegeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Override
    public List<PrivilegeDTO> listAll(String resourceType) {
        List<Privilege> list = this.privilegeMapper.selectList(new LambdaQueryWrapper<Privilege>()
                .eq(StrUtil.isNotEmpty(resourceType), Privilege::getResourceType, resourceType));
        return PrivilegeConvert.INSTANCE.toDto(list);
    }

}
