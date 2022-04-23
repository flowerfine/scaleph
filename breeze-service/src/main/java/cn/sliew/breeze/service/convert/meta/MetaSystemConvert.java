package cn.sliew.breeze.service.convert.meta;

import cn.sliew.breeze.dao.entity.MetaSystem;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.meta.MetaSystemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaSystemConvert extends BaseConvert<MetaSystem, MetaSystemDTO> {
    MetaSystemConvert INSTANCE = Mappers.getMapper(MetaSystemConvert.class);

}
