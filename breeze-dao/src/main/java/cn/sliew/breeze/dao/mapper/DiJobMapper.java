package cn.sliew.breeze.dao.mapper;

import cn.sliew.breeze.dao.entity.DiJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据集成-作业信息 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Repository
public interface DiJobMapper extends BaseMapper<DiJob> {
    /**
     * 自定义分页查询
     *
     * @param page page
     * @param job  job param
     * @return page
     */
    Page<DiJob> selectPage(IPage<?> page, @Param("job") DiJob job);
}
