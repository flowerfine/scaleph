package cn.sliew.scaleph.core.di.service.convert;

import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DiDirectoryConvert.class, DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobConvert extends BaseConvert<DiJob, DiJobDTO> {
    DiJobConvert INSTANCE = Mappers.getMapper(DiJobConvert.class);

    @Override
    @Mapping(source = "directory.id",target = "directoryId")
    DiJob toDo(DiJobDTO dto);

    @Override
    @Mapping(source = "directoryId", target = "directory.id")
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.JOB_TYPE,entity.getJobType()))", target = "jobType")
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.JOB_STATUS,entity.getJobStatus()))", target = "jobStatus")
    @Mapping(expression = "java(cn.sliew.scaleph.system.service.vo.DictVO.toVO(cn.sliew.scaleph.common.constant.DictConstants.RUNTIME_STATE,entity.getRuntimeState()))", target = "runtimeState")
    DiJobDTO toDto(DiJob entity);
}
