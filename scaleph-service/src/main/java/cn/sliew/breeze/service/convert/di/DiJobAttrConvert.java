package cn.sliew.breeze.service.convert.di;

import cn.sliew.breeze.dao.entity.DiJobAttr;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.di.DiJobAttrDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobAttrConvert extends BaseConvert<DiJobAttr, DiJobAttrDTO> {
    DiJobAttrConvert INSTANCE = Mappers.getMapper(DiJobAttrConvert.class);

    @Override
    @Mapping(expression = "java(cn.sliew.breeze.service.vo.DictVO.toVO(cn.sliew.breeze.common.constant.DictConstants.JOB_ATTR_TYPE,entity.getJobAttrType()))", target = "jobAttrType")
    DiJobAttrDTO toDto(DiJobAttr entity);
}
