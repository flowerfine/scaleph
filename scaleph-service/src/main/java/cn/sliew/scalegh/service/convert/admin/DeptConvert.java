package cn.sliew.scalegh.service.convert.admin;

import cn.sliew.breeze.dao.entity.master.security.Dept;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.admin.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface DeptConvert extends BaseConvert<Dept, DeptDTO> {
    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    @Override
    @Mapping(expression = "java(DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    DeptDTO toDto(Dept entity);
}
