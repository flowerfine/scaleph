package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.DiJobResourceFile;
import com.liyu.breeze.dao.entity.DiResourceFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据集成-作业资源 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-04-19
 */
@Repository
public interface DiJobResourceFileMapper extends BaseMapper<DiJobResourceFile> {

    List<DiResourceFile> listJobResources(@Param(value = "jobId") Long jobId);
}
