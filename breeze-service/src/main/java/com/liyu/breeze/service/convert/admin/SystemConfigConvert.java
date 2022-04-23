package com.liyu.breeze.service.convert.admin;

import com.liyu.breeze.dao.entity.SystemConfig;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.dto.admin.SystemConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SystemConfigConvert extends BaseConvert<SystemConfig, SystemConfigDTO> {

    SystemConfigConvert INSTANCE = Mappers.getMapper(SystemConfigConvert.class);
}
