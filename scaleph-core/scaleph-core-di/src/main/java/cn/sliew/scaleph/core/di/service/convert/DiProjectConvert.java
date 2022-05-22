package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.dao.entity.master.di.DiProject;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiProjectConvert extends BaseConvert<DiProject, DiProjectDTO> {
    DiProjectConvert INSTANCE = Mappers.getMapper(DiProjectConvert.class);

}
