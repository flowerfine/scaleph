package cn.sliew.scalegh.service.convert.di;

import cn.sliew.scalegh.dao.entity.master.di.DiJobAttr;
import cn.sliew.scalegh.service.convert.BaseConvert;
import cn.sliew.scalegh.service.convert.DictVoConvert;
import cn.sliew.scalegh.service.dto.di.DiJobAttrDTO;
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
    @Mapping(expression = "java(cn.sliew.scalegh.service.vo.DictVO.toVO(cn.sliew.scalegh.common.constant.DictConstants.JOB_ATTR_TYPE,entity.getJobAttrType()))", target = "jobAttrType")
    DiJobAttrDTO toDto(DiJobAttr entity);
}
