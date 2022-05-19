package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.system.SystemConfig;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.admin.SystemConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SystemConfigConvert extends BaseConvert<SystemConfig, SystemConfigDTO> {

    SystemConfigConvert INSTANCE = Mappers.getMapper(SystemConfigConvert.class);
}
