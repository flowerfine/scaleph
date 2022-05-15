package cn.sliew.scaleph.service.admin;


import cn.sliew.scaleph.service.dto.admin.DeptDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface DeptService {
    /**
     * 新增部门
     *
     * @param deptDTO dept
     * @return int
     */
    int insert(DeptDTO deptDTO);

    /**
     * 更新部门
     *
     * @param deptDTO dept
     * @return int
     */
    int update(DeptDTO deptDTO);

    /**
     * 删除部门
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 批量删除部门
     *
     * @param list ids
     * @return int
     */
    int deleteBatch(List<? extends Serializable> list);

    /**
     * 根据id查询
     *
     * @param id dept id
     * @return dept
     */
    DeptDTO selectOne(Long id);

    /**
     * 根据部门编码查询
     *
     * @param deptCode deptCode
     * @return dept
     */
    DeptDTO selectOne(String deptCode);

    /**
     * 查询全部部门信息
     *
     * @return all dept
     */
    List<DeptDTO> listAll();

    /**
     * 根据id查询下属一级部门信息
     *
     * @param pid dept pid
     * @return dept list
     */
    List<DeptDTO> listByDeptId(Long pid);
}
