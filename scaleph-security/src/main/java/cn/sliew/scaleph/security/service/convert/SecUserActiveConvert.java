package cn.sliew.scaleph.security.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.security.SecUserActive;
import cn.sliew.scaleph.security.service.dto.SecUserActiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface SecUserActiveConvert extends BaseConvert<SecUserActive, SecUserActiveDTO> {

    SecUserActiveConvert INSTANCE = Mappers.getMapper(SecUserActiveConvert.class);
}
