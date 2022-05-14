package cn.sliew.breeze.dao.mapper.master.di;

import cn.sliew.breeze.dao.entity.master.di.DiResourceFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据集成-资源 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-04-13
 */
@Repository
public interface DiResourceFileMapper extends BaseMapper<DiResourceFile> {

    Page<DiResourceFile> selectPage(IPage<?> page, @Param("projectId") Long projectId, @Param("fileName") String fileName);

}
