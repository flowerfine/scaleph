package cn.sliew.scalegh.service.convert.meta;

import cn.sliew.breeze.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.meta.MetaDataSetTypeDTO;
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
