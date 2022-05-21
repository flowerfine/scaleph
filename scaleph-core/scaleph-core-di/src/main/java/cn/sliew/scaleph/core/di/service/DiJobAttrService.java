package cn.sliew.scaleph.core.di.service;

import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据集成-作业参数 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobAttrService {

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
     * 查询作业参数
     *
     * @param jobId job id
     * @return job attr list
     */
    List<DiJobAttrDTO> listJobAttr(Long jobId);

    /**
     * 插入更新
     *
     * @param jobAttrDTO 参数
     * @return int
     */
    int upsert(DiJobAttrDTO jobAttrDTO);


}
