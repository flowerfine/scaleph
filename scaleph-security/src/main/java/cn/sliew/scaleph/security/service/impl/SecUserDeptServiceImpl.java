package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecUserDept;
import cn.sliew.scaleph.dao.mapper.master.security.SecUserDeptMapper;
import cn.sliew.scaleph.security.service.SecUserDeptService;
import cn.sliew.scaleph.security.service.convert.SecUserDeptConvert;
import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和部门关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecUserDeptServiceImpl implements SecUserDeptService {

    @Autowired
    private SecUserDeptMapper secUserDeptMapper;

    @Override
    public int insert(SecUserDeptDTO secUserDeptDTO) {
        SecUserDept secUserDept = SecUserDeptConvert.INSTANCE.toDo(secUserDeptDTO);
        return this.secUserDeptMapper.insert(secUserDept);
    }

    @Override
    public int deleteBydeptId(Serializable deptId) {
        return this.secUserDeptMapper.delete(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, deptId));
    }

    @Override
    public int delete(SecUserDeptDTO secUserDeptDTO) {
        return this.secUserDeptMapper.delete(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, secUserDeptDTO.getDeptId())
            .eq(SecUserDept::getUserId, secUserDeptDTO.getUserId())
        );
    }

    @Override
    public List<SecUserDeptDTO> listByDeptId(Serializable deptId) {
        List<SecUserDept> list = this.secUserDeptMapper.selectList(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, deptId));
        return SecUserDeptConvert.INSTANCE.toDto(list);
    }
}
