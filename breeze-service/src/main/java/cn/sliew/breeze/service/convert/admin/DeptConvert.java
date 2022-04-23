package cn.sliew.breeze.service.convert.admin;

import cn.sliew.breeze.dao.entity.Dept;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.admin.DeptDTO;
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
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    DeptDTO toDto(Dept entity);
}
