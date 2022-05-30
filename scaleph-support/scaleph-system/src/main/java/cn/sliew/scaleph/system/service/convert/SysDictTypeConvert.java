package cn.sliew.scaleph.system.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.system.SysDictType;
import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictTypeConvert extends BaseConvert<SysDictType, SysDictTypeDTO> {

    SysDictTypeConvert INSTANCE = Mappers.getMapper(SysDictTypeConvert.class);
}
