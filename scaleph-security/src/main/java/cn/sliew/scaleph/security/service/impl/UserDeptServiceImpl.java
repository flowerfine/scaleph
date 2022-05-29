package cn.sliew.scaleph.security.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.UserDept;
import cn.sliew.scaleph.dao.mapper.master.security.UserDeptMapper;
import cn.sliew.scaleph.security.service.UserDeptService;
import cn.sliew.scaleph.security.service.convert.UserDeptConvert;
import cn.sliew.scaleph.security.service.dto.UserDeptDTO;
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
