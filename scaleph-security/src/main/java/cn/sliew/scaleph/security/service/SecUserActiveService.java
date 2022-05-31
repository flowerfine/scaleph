package cn.sliew.scaleph.security.service;

import cn.sliew.scaleph.security.service.dto.SecUserActiveDTO;

/**
 * <p>
 * 用户邮箱激活日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
public interface SecUserActiveService {
    /**
     * 新增
     *
     * @param secUserActiveDTO
     * @return
     */
    int insert(SecUserActiveDTO secUserActiveDTO);

    /**
     * 修改
     *
     * @param secUserActiveDTO
     * @return
     */
    int updateByUserAndCode(SecUserActiveDTO secUserActiveDTO);

    /**
     * 查询用户激活码信息
     *
     * @param userName
     * @param activeCode
     * @return
     */
    SecUserActiveDTO selectOne(String userName, String activeCode);
}
