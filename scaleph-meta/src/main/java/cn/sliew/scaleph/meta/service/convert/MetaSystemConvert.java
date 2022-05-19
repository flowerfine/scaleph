package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.scaleph.dao.entity.master.meta.MetaSystem;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.meta.service.dto.MetaSystemDTO;
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
