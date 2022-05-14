package cn.sliew.scalegh.service.convert.di;

import cn.sliew.scalegh.dao.entity.master.di.DiJobLink;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.di.DiJobLinkDTO;
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
