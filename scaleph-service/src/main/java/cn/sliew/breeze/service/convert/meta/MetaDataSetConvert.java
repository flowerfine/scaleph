package cn.sliew.breeze.service.convert.meta;

import cn.sliew.breeze.dao.entity.MetaDataSet;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataSetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, MetaSystemConvert.class, MetaDataSetTypeConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSetConvert extends BaseConvert<MetaDataSet, MetaDataSetDTO> {
    MetaDataSetConvert INSTANCE = Mappers.getMapper(MetaDataSetConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.YES_NO,entity.getIsStandard()))", target = "isStandard")
    @Mapping(source = "dataSetType", target = "dataSetType")
    @Mapping(source = "system", target = "system")
    MetaDataSetDTO toDto(MetaDataSet entity);

    @Override
    @Mapping(source = "dataSetType.id", target = "dataSetTypeId")
    @Mapping(source = "system.id", target = "systemId")
    MetaDataSet toDo(MetaDataSetDTO dto);
}
