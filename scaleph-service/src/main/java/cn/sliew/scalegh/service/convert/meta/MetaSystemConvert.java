package cn.sliew.scalegh.service.convert.meta;

import cn.sliew.scalegh.dao.entity.master.meta.MetaSystem;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.meta.MetaSystemDTO;
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
