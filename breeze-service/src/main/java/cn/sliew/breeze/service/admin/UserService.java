package cn.sliew.breeze.service.admin;

import cn.sliew.breeze.service.dto.admin.RoleDTO;
import cn.sliew.breeze.service.dto.admin.UserDTO;
import cn.sliew.breeze.service.param.admin.UserParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface UserService {
    /**
     * 新增用户
     *
     * @param userDTO user
     * @return int
     */
    int insert(UserDTO userDTO);

    /**
     * 修改用户
     *
     * @param userDTO user
     * @return int
     */
    int update(UserDTO userDTO);

    /**
     * 修改用户
     *
     * @param userDTO user
     * @return int
     */
    int updateByUserName(UserDTO userDTO);

    /**
     * 逻辑删除用户，修改用户状态为注销
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 逻辑删除用户，修改用户状态为注销
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据id查询用户
     *
     * @param id user id
     * @return user
     */
    UserDTO selectOne(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param userName user name
     * @return user
     */
    UserDTO selectOne(String userName);

    /**
     * 分页查询
     *
     * @param userParam 查询参数
     * @return page list
     */
    Page<UserDTO> listByPage(UserParam userParam);

    /**
     * 根据email查询用户
     *
     * @param email email
     * @return user
     */
    UserDTO selectByEmail(String email);

    /**
     * 根据角色查询用户列表
     *
     * @param roleId    role
     * @param direction 0 1
     * @param userName  username
     * @return list
     */
    List<UserDTO> listByRole(Long roleId, String userName, String direction);

    /**
     * 根据dept查询用户列表
     *
     * @param deptId    dept
     * @param direction 0 1
     * @param userName  username
     * @return list
     */
    List<UserDTO> listByDept(Long deptId, String userName, String direction);


    /**
     * 根据用户名查询用户列表
     *
     * @param userName dept
     * @return list
     */
    List<UserDTO> listByUserName(String userName);

    /**
     * 获取用户名对应的全部角色权限信息
     *
     * @param userName user name
     * @return role list
     */
    List<RoleDTO> getAllPrivilegeByUserName(String userName);

}
