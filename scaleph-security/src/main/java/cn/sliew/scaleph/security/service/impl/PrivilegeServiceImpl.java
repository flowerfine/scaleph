package cn.sliew.scaleph.security.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.security.Privilege;
import cn.sliew.scaleph.dao.mapper.master.security.PrivilegeMapper;
import cn.sliew.scaleph.security.service.PrivilegeService;
import cn.sliew.scaleph.security.service.convert.PrivilegeConvert;
import cn.sliew.scaleph.security.service.dto.PrivilegeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
