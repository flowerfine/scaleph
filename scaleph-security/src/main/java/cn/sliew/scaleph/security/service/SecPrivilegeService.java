package cn.sliew.scaleph.security.service;

import java.util.List;

import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecPrivilegeService {
    /**
     * 查询全部权限编码
     *
     * @param resourceType 资源类型
     * @return list
     */
    List<SecPrivilegeDTO> listAll(String resourceType);

}
