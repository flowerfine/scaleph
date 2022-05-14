package cn.sliew.breeze.dao.mapper;

import cn.sliew.breeze.dao.entity.DiJobStep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据集成-作业步骤信息 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Repository
public interface DiJobStepMapper extends BaseMapper<DiJobStep> {
    /**
     * 按项目id删除
     *
     * @param projectIds project id
     * @return int
     */
    int deleteByProjectId(@Param("projectIds") Collection<? extends Serializable> projectIds);

    /**
     * 按job id 删除
     *
     * @param jobIds job id
     * @return int
     */
    int deleteByJobId(@Param("jobIds") Collection<? extends Serializable> jobIds);

    /**
     * 查询作业id相关的步骤
     *
     * @param jobId job id
     * @return job step list
     */
    List<DiJobStep> selectByJobId(@Param("jobId") Long jobId);
}
