package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.SystemConfig;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.admin.SystemConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SystemConfigConvert extends BaseConvert<SystemConfig, SystemConfigDTO> {

    SystemConfigConvert INSTANCE = Mappers.getMapper(SystemConfigConvert.class);
}
