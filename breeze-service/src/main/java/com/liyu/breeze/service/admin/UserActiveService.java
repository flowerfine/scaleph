package com.liyu.breeze.service.admin;

import com.liyu.breeze.service.dto.admin.UserActiveDTO;

/**
 * <p>
 * 用户邮箱激活日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
public interface UserActiveService {
    /**
     * 新增
     *
     * @param userActiveDTO
     * @return
     */
    int insert(UserActiveDTO userActiveDTO);

    /**
     * 修改
     *
     * @param userActiveDTO
     * @return
     */
    int updateByUserAndCode(UserActiveDTO userActiveDTO);

    /**
     * 查询用户激活码信息
     *
     * @param userName
     * @param activeCode
     * @return
     */
    UserActiveDTO selectOne(String userName, String activeCode);
}
