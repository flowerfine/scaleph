package cn.sliew.scaleph.dao.mapper.master.di;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据集成-作业运行日志 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-05-06
 */
@Repository
public interface DiJobLogMapper extends BaseMapper<DiJobLog> {

    Page<DiJobLog> selectPage(IPage<?> page,
                              @Param(value = "log") DiJobLog log,
                              @Param(value = "jobType") String jobType
    );

    List<DiJobLog> selectTopN(@Param(value = "jobType") String jobType,
                              @Param(value = "n") Integer n,
                              @Param(value = "startTime") Date startTime);

    List<Map<String, Object>> selectRealtimeJobRuntimeStatus(
        @Param(value = "jobType") String jobType);
}
