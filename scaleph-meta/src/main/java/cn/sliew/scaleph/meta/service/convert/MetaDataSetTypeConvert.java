package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSetTypeConvert extends BaseConvert<MetaDataSetType, MetaDataSetTypeDTO> {
    MetaDataSetTypeConvert INSTANCE = Mappers.getMapper(MetaDataSetTypeConvert.class);

}
