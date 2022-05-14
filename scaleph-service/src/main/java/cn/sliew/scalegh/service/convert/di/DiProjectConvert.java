package cn.sliew.scalegh.service.convert.di;

import cn.sliew.scalegh.dao.entity.master.di.DiProject;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.di.DiProjectDTO;
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
