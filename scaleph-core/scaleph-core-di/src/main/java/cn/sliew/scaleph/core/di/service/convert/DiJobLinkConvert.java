package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobLink;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobLinkConvert extends BaseConvert<DiJobLink, DiJobLinkDTO> {
    DiJobLinkConvert INSTANCE = Mappers.getMapper(DiJobLinkConvert.class);

}
