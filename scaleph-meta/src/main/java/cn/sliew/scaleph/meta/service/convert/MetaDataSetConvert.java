package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.meta.service.dto.MetaDataSetDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, MetaSystemConvert.class,
    MetaDataSetTypeConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSetConvert extends BaseConvert<MetaDataSet, MetaDataSetDTO> {
    MetaDataSetConvert INSTANCE = Mappers.getMapper(MetaDataSetConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.YES_NO,entity.getIsStandard()))", target = "isStandard")
    @Mapping(source = "dataSetType", target = "dataSetType")
    @Mapping(source = "system", target = "system")
    MetaDataSetDTO toDto(MetaDataSet entity);

    @Override
    @Mapping(source = "dataSetType.id", target = "dataSetTypeId")
    @Mapping(source = "system.id", target = "systemId")
    MetaDataSet toDo(MetaDataSetDTO dto);
}
