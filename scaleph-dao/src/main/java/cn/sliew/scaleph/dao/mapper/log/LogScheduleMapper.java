package cn.sliew.scaleph.dao.mapper.log;

import cn.sliew.scaleph.dao.entity.log.LogSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 定时任务运行日志表 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-10-16
 */
@Repository
public interface LogScheduleMapper extends BaseMapper<LogSchedule> {

}
