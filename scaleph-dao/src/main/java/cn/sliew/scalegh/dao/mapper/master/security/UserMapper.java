package cn.sliew.scalegh.dao.mapper.master.security;

import cn.sliew.scalegh.dao.entity.master.security.Role;
import cn.sliew.scalegh.dao.entity.master.security.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户基本信息表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 批量修改用户状态
     *
     * @param idList     用户id列表
     * @param userStatus 用户状态
     * @return int
     */
    @Update({"<script>" +
            "update t_user " +
            "set user_status = #{userStatus} " +
            "where id in " +
            "<foreach item=\"id\" index=\"index\" collection =\"idList\" open=\"(\" close=\")\" separator=\",\"> #{id}</foreach>" +
            "</script>"})
    int batchUpdateUserStatus(@Param("idList") List<Integer> idList, @Param("userStatus") String userStatus);

    /**
     * 分页查询
     *
     * @param page   分页参数
     * @param deptId 部门编码
     * @param roleId 角色编码
     * @param user   用户参数
     * @return list
     */
    Page<User> selectPage(IPage<?> page, @Param("deptId") String deptId, @Param("roleId") String roleId, @Param("user") User user);

    /**
     * 根据角色或者部门id查询
     *
     * @param deptId    部门id
     * @param roleId    角色id
     * @param userName  userName
     * @param direction 1:target 0:source
     * @return list
     */
    List<User> selectByRoleOrDept(@Param("deptId") String deptId, @Param("roleId") String roleId, @Param("userName") String userName, @Param("direction") String direction);

    /**
     * 查询用户对应所有角色权限信息
     *
     * @param userName user name
     * @return role list
     */
    List<Role> selectAllPrivilege(@Param("userName") String userName);
}
