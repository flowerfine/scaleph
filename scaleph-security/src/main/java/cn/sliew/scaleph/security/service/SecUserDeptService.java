package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;

/**
 * <p>
 * 用户和部门关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecUserDeptService {
    /**
     * 新增用户部门关系
     *
     * @param secUserDeptDTO user dept
     * @return int
     */
    int insert(SecUserDeptDTO secUserDeptDTO);

    /**
     * 根据部门删除
     *
     * @param deptId deptid
     * @return int
     */
    int deleteBydeptId(Serializable deptId);

    /**
     * 删除用户部门关系
     *
     * @param secUserDeptDTO user dept
     * @return int
     */
    int delete(SecUserDeptDTO secUserDeptDTO);

    /**
     * 查询部门下用户列表
     *
     * @param deptId dept
     * @return list
     */
    List<SecUserDeptDTO> listByDeptId(Serializable deptId);
}
