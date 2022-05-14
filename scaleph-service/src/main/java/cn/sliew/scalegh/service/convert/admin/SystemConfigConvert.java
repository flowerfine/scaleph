package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.scalegh.dao.entity.master.system.SystemConfig;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.admin.SystemConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SystemConfigConvert extends BaseConvert<SystemConfig, SystemConfigDTO> {

    SystemConfigConvert INSTANCE = Mappers.getMapper(SystemConfigConvert.class);
}
