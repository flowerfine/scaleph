package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiDirectory;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.dto.di.DiDirectoryDTO;
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
