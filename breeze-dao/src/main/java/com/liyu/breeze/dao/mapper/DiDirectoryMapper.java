package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.DiDirectory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据集成-项目目录 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Repository
public interface DiDirectoryMapper extends BaseMapper<DiDirectory> {
    /**
     * 获取目录对应的所有上级节点信息
     *
     * @param ids directory ids
     * @return directory list
     */
    List<DiDirectory> selectFullPath(@Param("ids") List<Long> ids);
}
