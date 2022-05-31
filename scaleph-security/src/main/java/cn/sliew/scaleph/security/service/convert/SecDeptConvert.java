package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecDept;
import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = DictVoConvert.class)
public interface SecDeptConvert extends BaseConvert<SecDept, SecDeptDTO> {
    SecDeptConvert INSTANCE = Mappers.getMapper(SecDeptConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.DEPT_STATUS,entity.getDeptStatus()))", target = "deptStatus")
    SecDeptDTO toDto(SecDept entity);
}
