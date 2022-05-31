package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecUserDept;
import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SecUserDeptConvert extends BaseConvert<SecUserDept, SecUserDeptDTO> {

    SecUserDeptConvert INSTANCE = Mappers.getMapper(SecUserDeptConvert.class);
}
