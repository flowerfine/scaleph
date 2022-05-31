package cn.sliew.scaleph.security.service.impl;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecPrivilege;
import cn.sliew.scaleph.dao.mapper.master.security.SecPrivilegeMapper;
import cn.sliew.scaleph.security.service.SecPrivilegeService;
import cn.sliew.scaleph.security.service.convert.SecPrivilegeConvert;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
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
public class SecPrivilegeServiceImpl implements SecPrivilegeService {

    @Autowired
    private SecPrivilegeMapper secPrivilegeMapper;

    @Override
    public List<SecPrivilegeDTO> listAll(String resourceType) {
        List<SecPrivilege> list = this.secPrivilegeMapper.selectList(new LambdaQueryWrapper<SecPrivilege>()
            .eq(StrUtil.isNotEmpty(resourceType), SecPrivilege::getResourceType, resourceType));
        return SecPrivilegeConvert.INSTANCE.toDto(list);
    }

}
