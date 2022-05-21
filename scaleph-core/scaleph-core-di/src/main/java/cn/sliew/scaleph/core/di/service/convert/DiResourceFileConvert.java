package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.dao.entity.master.di.DiResourceFile;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiResourceFileConvert extends BaseConvert<DiResourceFile, DiResourceFileDTO> {
    DiResourceFileConvert INSTANCE = Mappers.getMapper(DiResourceFileConvert.class);

}
