package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecDept;
import cn.sliew.scaleph.dao.mapper.master.security.SecDeptMapper;
import cn.sliew.scaleph.security.service.SecDeptRoleService;
import cn.sliew.scaleph.security.service.SecDeptService;
import cn.sliew.scaleph.security.service.SecUserDeptService;
import cn.sliew.scaleph.security.service.convert.SecDeptConvert;
import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecDeptServiceImpl implements SecDeptService {

    @Autowired
    private SecDeptMapper secDeptMapper;
    @Autowired
    private SecUserDeptService secUserDeptService;
    @Autowired
    private SecDeptRoleService secDeptRoleService;

    @Override
    public int insert(SecDeptDTO secDeptDTO) {
        SecDept secDept = SecDeptConvert.INSTANCE.toDo(secDeptDTO);
        return this.secDeptMapper.insert(secDept);
    }

    @Override
    public int update(SecDeptDTO secDeptDTO) {
        SecDept secDept = SecDeptConvert.INSTANCE.toDo(secDeptDTO);
        return this.secDeptMapper.updateById(secDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        this.secUserDeptService.deleteBydeptId(id);
        this.secDeptRoleService.deleteByDeptId(id);
        return this.secDeptMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(List<? extends Serializable> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        for (Serializable id : list) {
            this.secUserDeptService.deleteBydeptId(id);
            this.secDeptRoleService.deleteByDeptId(id);
        }
        return this.secDeptMapper.deleteBatchIds(list);
    }

    @Override
    public SecDeptDTO selectOne(Long id) {
        SecDept secDept = this.secDeptMapper.selectById(id);
        return SecDeptConvert.INSTANCE.toDto(secDept);
    }

    @Override
    public SecDeptDTO selectOne(String deptCode) {
        SecDept secDept = this.secDeptMapper.selectOne(new LambdaQueryWrapper<SecDept>()
            .eq(SecDept::getDeptCode, deptCode));
        return SecDeptConvert.INSTANCE.toDto(secDept);
    }

    @Override
    public List<SecDeptDTO> listAll() {
        List<SecDept> secDeptList = this.secDeptMapper.selectList(null);
        return SecDeptConvert.INSTANCE.toDto(secDeptList);
    }

    @Override
    public List<SecDeptDTO> listByDeptId(Long pid) {
        List<SecDept> secDeptList = this.secDeptMapper.selectList(new LambdaQueryWrapper<SecDept>()
            .eq(SecDept::getPid, pid));
        return SecDeptConvert.INSTANCE.toDto(secDeptList);
    }
}
