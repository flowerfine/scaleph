package com.liyu.breeze.service.convert.di;

import com.liyu.breeze.dao.entity.DiDirectory;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.di.DiDirectoryDTO;
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
