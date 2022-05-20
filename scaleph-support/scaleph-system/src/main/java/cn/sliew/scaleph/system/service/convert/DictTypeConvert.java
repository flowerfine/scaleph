package cn.sliew.scaleph.system.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.system.DictType;
import cn.sliew.scaleph.system.service.dto.DictTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictTypeConvert extends BaseConvert<DictType, DictTypeDTO> {

    DictTypeConvert INSTANCE = Mappers.getMapper(DictTypeConvert.class);
}
