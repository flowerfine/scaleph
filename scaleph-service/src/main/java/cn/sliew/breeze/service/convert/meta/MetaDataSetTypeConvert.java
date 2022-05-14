package cn.sliew.breeze.service.convert.meta;

import cn.sliew.breeze.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataSetTypeDTO;
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
