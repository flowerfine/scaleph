package cn.sliew.scaleph.service.convert.admin;

import cn.sliew.scaleph.dao.entity.master.security.Dept;
import cn.sliew.scaleph.service.convert.BaseConvert;
import cn.sliew.scaleph.service.convert.DictVoConvert;
import cn.sliew.scaleph.service.dto.admin.DeptDTO;
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
    @Mapping(expression = "java(cn.sliew.scaleph.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    DeptDTO toDto(Dept entity);
}
