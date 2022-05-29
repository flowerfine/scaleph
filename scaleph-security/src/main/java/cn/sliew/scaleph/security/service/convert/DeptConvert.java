package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.Dept;
import cn.sliew.scaleph.security.service.dto.DeptDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
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
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    DeptDTO toDto(Dept entity);
}
