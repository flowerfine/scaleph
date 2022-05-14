package cn.sliew.scalegh.service.convert.meta;

import cn.sliew.scalegh.dao.entity.master.meta.MetaDataMap;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.meta.MetaDataMapDTO;
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
