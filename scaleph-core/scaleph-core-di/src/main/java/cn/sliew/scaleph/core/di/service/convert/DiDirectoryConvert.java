package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiDirectory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiDirectoryConvert extends BaseConvert<DiDirectory, DiDirectoryDTO> {
    DiDirectoryConvert INSTANCE = Mappers.getMapper(DiDirectoryConvert.class);

}
