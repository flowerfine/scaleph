package cn.sliew.scaleph.system.service;

import cn.sliew.scaleph.system.service.dto.SysConfigDTO;

/**
 * <p>
 * 系统配置信息表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-10
 */
public interface SysConfigService {

    int insert(SysConfigDTO sysConfigDTO);

    int updateByCode(SysConfigDTO sysConfigDTO);

    int deleteByCode(String code);

    SysConfigDTO selectByCode(String code);

    String getSeatunnelHome();
}
