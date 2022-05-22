package cn.sliew.scaleph.core.di.service;

import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据集成-作业步骤信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobStepService {

    /**
     * 更新
     *
     * @param diJobStepDTO job step info
     * @return int
     */
    int update(DiJobStepDTO diJobStepDTO);

    /**
     * 插入更新
     *
     * @param diJobStep job step info
     * @return int
     */
    int upsert(DiJobStepDTO diJobStep);

    /**
     * 按项目id删除
     *
     * @param projectIds project id
     * @return int
     */
    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    /**
     * 按job id 删除
     *
     * @param jobIds job id
     * @return int
     */
    int deleteByJobId(Collection<? extends Serializable> jobIds);

    /**
     * 删除多余的步骤信息
     *
     * @param jobId        job id
     * @param stepCodeList step code list
     * @return int
     */
    int deleteSurplusStep(Long jobId, List<String> stepCodeList);

    /**
     * 查询作业步骤信息
     *
     * @param jobId job id
     * @return job step list
     */
    List<DiJobStepDTO> listJobStep(Long jobId);

    /**
     * 查询步骤信息
     *
     * @param jobId    job id
     * @param stepCode stepCode
     * @return DiJobStepDTO
     */
    DiJobStepDTO selectOne(Long jobId, String stepCode);

    int clone(Long sourceJobId, Long targetJobId);

}
