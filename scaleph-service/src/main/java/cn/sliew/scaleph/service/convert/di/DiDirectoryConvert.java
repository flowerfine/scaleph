package cn.sliew.scaleph.service.convert.di;

import cn.sliew.scaleph.dao.entity.master.di.DiDirectory;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.dto.di.DiDirectoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiDirectoryConvert extends BaseConvert<DiDirectory, DiDirectoryDTO> {
    DiDirectoryConvert INSTANCE = Mappers.getMapper(DiDirectoryConvert.class);

}
