package cn.sliew.breeze.service.convert.meta;

import cn.sliew.breeze.dao.entity.MetaDataMap;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataMapDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataMapConvert extends BaseConvert<MetaDataMap, MetaDataMapDTO> {
    MetaDataMapConvert INSTANCE = Mappers.getMapper(MetaDataMapConvert.class);

}
