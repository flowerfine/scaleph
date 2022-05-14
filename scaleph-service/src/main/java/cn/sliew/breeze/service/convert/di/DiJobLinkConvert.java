package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiJobLink;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.di.DiJobLinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobLinkConvert extends BaseConvert<DiJobLink, DiJobLinkDTO> {
    DiJobLinkConvert INSTANCE = Mappers.getMapper(DiJobLinkConvert.class);

}
