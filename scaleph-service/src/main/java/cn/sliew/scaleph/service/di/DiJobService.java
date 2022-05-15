package cn.sliew.scaleph.service.di;

import cn.sliew.scaleph.service.dto.di.DiJobDTO;
import cn.sliew.scaleph.service.param.di.DiJobParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据集成-作业信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobService {
    /**
     * 新增
     *
     * @param dto info
     * @return int
     */
    DiJobDTO insert(DiJobDTO dto);

    /**
     * 归档任务，只保留发布状态中最大版本号的那个，其余发布状态的任务均改为归档状态
     *
     * @param jobCode job code
     * @return int
     */
    int archive(String jobCode);

    /**
     * 修改
     *
     * @param dto info
     * @return int
     */
    int update(DiJobDTO dto);

    /**
     * 删除一个
     *
     * @param jobCode jobCode
     * @return int
     */
    int deleteByCode(String jobCode);

    /**
     * 批量删除
     *
     * @param list ids
     * @return int
     */
    int deleteByCode(List<DiJobDTO> list);

    /**
     * 物理删除项目下所有作业
     *
     * @param projectIds project id
     * @return int
     */
    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    /**
     * 分页
     *
     * @param param param
     * @return page list
     */
    Page<DiJobDTO> listByPage(DiJobParam param);

    /**
     * 返回id对应作业列表
     *
     * @param ids ids
     * @return list
     */
    List<DiJobDTO> listById(Collection<? extends Serializable> ids);

    /**
     * 查询job
     *
     * @param id id
     * @return info
     */
    DiJobDTO selectOne(Long id);


    /**
     * 项目下是否有有效的作业
     *
     * @param projectIds project iD
     * @return boolean
     */
    boolean hasValidJob(Collection<Long> projectIds);

    /**
     * 目录下是否存在有效作业
     *
     * @param projectId project id
     * @param dirId     dir id
     * @return boolean
     */
    boolean hasValidJob(Long projectId, Long dirId);

    /**
     * 集群下是否有正在运行的作业
     *
     * @param clusterIds cluster ids
     * @return boolean
     */
    boolean hasRunningJob(Collection<Long> clusterIds);

    int totalCnt(String jobType);
}
