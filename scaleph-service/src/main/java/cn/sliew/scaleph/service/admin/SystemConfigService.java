package cn.sliew.scaleph.service.admin;

import cn.sliew.scaleph.service.dto.admin.SystemConfigDTO;

/**
 * <p>
 * 系统配置信息表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-10
 */
public interface SystemConfigService {

    int insert(SystemConfigDTO systemConfigDTO);

    int updateByCode(SystemConfigDTO systemConfigDTO);

    int deleteByCode(String code);

    SystemConfigDTO selectByCode(String code);

    String getSeatunnelHome();
}
