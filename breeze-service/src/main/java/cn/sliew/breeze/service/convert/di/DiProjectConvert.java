package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiProject;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.di.DiProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiProjectConvert extends BaseConvert<DiProject, DiProjectDTO> {
    DiProjectConvert INSTANCE = Mappers.getMapper(DiProjectConvert.class);

}
