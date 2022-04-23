package com.liyu.breeze.service.convert.meta;

import com.liyu.breeze.dao.entity.MetaSystem;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.meta.MetaSystemDTO;
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
