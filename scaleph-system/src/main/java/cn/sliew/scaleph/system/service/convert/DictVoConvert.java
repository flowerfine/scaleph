package cn.sliew.scaleph.system.service.convert;

import cn.sliew.scaleph.system.service.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper
public interface DictVoConvert {

    DictVoConvert INSTANCE = Mappers.getMapper(DictVoConvert.class);

    default String toDo(DictVO vo) {
        if (vo == null) {
            return null;
        }
        return vo.getValue();
    }
}
