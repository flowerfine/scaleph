package com.liyu.breeze.service.convert.di;

import com.liyu.breeze.dao.entity.DiJob;
import com.liyu.breeze.service.convert.BaseConvert;
import com.liyu.breeze.service.convert.DictVoConvert;
import com.liyu.breeze.service.dto.di.DiJobDTO;
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
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.JOB_TYPE,entity.getJobType()))", target = "jobType")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.JOB_STATUS,entity.getJobStatus()))", target = "jobStatus")
    @Mapping(expression = "java(com.liyu.breeze.service.vo.DictVO.toVO(com.liyu.breeze.common.constant.DictConstants.RUNTIME_STATE,entity.getRuntimeState()))", target = "runtimeState")
    DiJobDTO toDto(DiJob entity);
}
