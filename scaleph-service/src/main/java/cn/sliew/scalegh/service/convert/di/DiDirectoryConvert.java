package cn.sliew.scalegh.service.convert.di;

import cn.sliew.scalegh.dao.entity.master.di.DiDirectory;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.dto.di.DiDirectoryDTO;
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
