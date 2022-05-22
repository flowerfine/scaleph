package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataMap;
import cn.sliew.scaleph.meta.service.dto.MetaDataMapDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataMapConvert extends BaseConvert<MetaDataMap, MetaDataMapDTO> {
    MetaDataMapConvert INSTANCE = Mappers.getMapper(MetaDataMapConvert.class);

}
