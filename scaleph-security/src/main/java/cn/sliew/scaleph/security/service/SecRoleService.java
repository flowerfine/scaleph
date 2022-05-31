package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.security.service.dto.SecRoleDTO;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecRoleService {
    /**
     * 新增角色
     *
     * @param secRoleDTO role
     * @return int
     */
    int insert(SecRoleDTO secRoleDTO);

    /**
     * 修改角色
     *
     * @param secRoleDTO role
     * @return int
     */
    int update(SecRoleDTO secRoleDTO);

    /**
     * 删除角色
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 批量删除角色
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据id查询
     *
     * @param id role id
     * @return role
     */
    SecRoleDTO selectOne(Long id);

    /**
     * 根据角色编码查询
     *
     * @param roleCode roleCode
     * @return role
     */
    SecRoleDTO selectOne(String roleCode);

    /**
     * 查询全部角色
     *
     * @return list
     */
    List<SecRoleDTO> listAll();

    /**
     * 查询部门对应角色
     *
     * @param grant  是否授权
     * @param deptId dept id
     * @return list
     */
    List<SecRoleDTO> selectRoleByDept(String grant, Long deptId);
}
