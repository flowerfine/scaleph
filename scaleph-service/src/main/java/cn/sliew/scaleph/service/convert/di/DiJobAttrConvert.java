package cn.sliew.scaleph.service.convert.di;

import cn.sliew.scaleph.dao.entity.master.di.DiJobAttr;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.service.convert.DictVoConvert;
import cn.sliew.scaleph.service.dto.di.DiJobAttrDTO;
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
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.JOB_ATTR_TYPE,entity.getJobAttrType()))", target = "jobAttrType")
    DiJobAttrDTO toDto(DiJobAttr entity);
}
