package cn.sliew.scaleph.security.service;

import cn.sliew.scaleph.security.service.dto.PrivilegeDTO;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface PrivilegeService {
    /**
     * 查询全部权限编码
     *
     * @param resourceType 资源类型
     * @return list
     */
    List<PrivilegeDTO> listAll(String resourceType);

}
