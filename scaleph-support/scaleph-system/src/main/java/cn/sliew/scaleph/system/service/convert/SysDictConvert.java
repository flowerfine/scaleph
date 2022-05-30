package cn.sliew.scaleph.system.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.system.SysDict;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {SysDictTypeConvert.class,
    DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictConvert extends BaseConvert<SysDict, SysDictDTO> {

    SysDictConvert INSTANCE = Mappers.getMapper(SysDictConvert.class);

    @Mapping(source = "dictType.dictTypeCode", target = "dictTypeCode")
    @Override
    SysDict toDo(SysDictDTO dto);

    @Mapping(source = "dictType", target = "dictType")
    @Mapping(source = "dictTypeCode", target = "dictType.dictTypeCode")
    @Override
    SysDictDTO toDto(SysDict entity);
}
