package cn.sliew.scaleph.dao.mapper.master.di;

import cn.sliew.scaleph.dao.entity.master.di.DiJobResourceFile;
import cn.sliew.scaleph.dao.entity.master.di.DiResourceFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
