package com.liyu.breeze.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyu.breeze.dao.entity.ScheduleLog;
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
public interface ScheduleLogMapper extends BaseMapper<ScheduleLog> {

}
