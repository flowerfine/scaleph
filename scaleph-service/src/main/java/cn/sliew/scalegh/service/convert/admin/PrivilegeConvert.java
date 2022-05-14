package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.Privilege;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.PrivilegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface PrivilegeConvert extends BaseConvert<Privilege, PrivilegeDTO> {

    PrivilegeConvert INSTANCE = Mappers.getMapper(PrivilegeConvert.class);

    @Override
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.RESOURCE_TYPE,entity.getResourceType()))", target = "resourceType")
    PrivilegeDTO toDto(Privilege entity);
}
