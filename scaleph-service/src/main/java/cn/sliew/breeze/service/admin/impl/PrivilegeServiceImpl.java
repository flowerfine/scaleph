package cn.sliew.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.Privilege;
import cn.sliew.breeze.dao.mapper.PrivilegeMapper;
import cn.sliew.breeze.service.admin.PrivilegeService;
import cn.sliew.breeze.service.convert.admin.PrivilegeConvert;
import cn.sliew.breeze.service.dto.admin.PrivilegeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
