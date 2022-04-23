package com.liyu.breeze.service.di;

import com.liyu.breeze.service.dto.di.DiJobStepAttrDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据集成-作业步骤参数 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobStepAttrService {

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
     * 删除多余的步骤属性
     *
     * @param jobId        job id
     * @param linkStepList step list
     * @return int
     */
    int deleteSurplusStepAttr(Long jobId, List<String> linkStepList);

    /**
     * 插入更新
     *
     * @param diJobStepAttr step attr
     * @return int
     */
    int upsert(DiJobStepAttrDTO diJobStepAttr);

    /**
     * 查询步骤属性列表地
     *
     * @param jobId    job id
     * @param stepCode step code
     * @return list
     */
    List<DiJobStepAttrDTO> listJobStepAttr(Long jobId, String stepCode);

}
