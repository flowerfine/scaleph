package cn.sliew.scaleph.system.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.system.SysConfig;
import cn.sliew.scaleph.system.service.dto.SysConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SysConfigConvert extends BaseConvert<SysConfig, SysConfigDTO> {

    SysConfigConvert INSTANCE = Mappers.getMapper(SysConfigConvert.class);
}
