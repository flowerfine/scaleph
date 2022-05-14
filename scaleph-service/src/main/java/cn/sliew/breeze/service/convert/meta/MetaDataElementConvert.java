package cn.sliew.breeze.service.convert.meta;

import cn.sliew.breeze.dao.entity.master.meta.MetaDataElement;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataElementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class, MetaDataSetTypeConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataElementConvert extends BaseConvert<MetaDataElement, MetaDataElementDTO> {
    MetaDataElementConvert INSTANCE = Mappers.getMapper(MetaDataElementConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.YES_NO,entity.getNullable()))", target = "nullable")
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.DATA_TYPE,entity.getDataType()))", target = "dataType")
    @Mapping(source = "dataSetType", target = "dataSetType")
    MetaDataElementDTO toDto(MetaDataElement entity);

    @Override
    @Mapping(source = "dataSetType.id", target = "dataSetTypeId")
    MetaDataElement toDo(MetaDataElementDTO dto);
}
