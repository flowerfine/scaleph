package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.DiJobAttr;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 数据集成-作业参数 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Repository
public interface DiJobAttrMapper extends BaseMapper<DiJobAttr> {

    /**
     * 按项目id删除
     *
     * @param projectIds project id
     * @return int
     */
    int deleteByProjectId(@Param("projectIds") Collection<? extends Serializable> projectIds);

    /**
     * 按job id 删除
     * @param jobIds job id
     * @return int
     */
    int deleteByJobId(@Param("jobIds") Collection<? extends Serializable> jobIds);
}
