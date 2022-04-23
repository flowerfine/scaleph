package com.liyu.breeze.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyu.breeze.service.dto.admin.ScheduleLogDTO;
import com.liyu.breeze.service.param.admin.ScheduleLogParam;

/**
 * <p>
 * 定时任务运行日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-16
 */
public interface ScheduleLogService {
    /**
     * 新增
     *
     * @param scheduleLog 日志对象
     * @return int
     */
    int insert(ScheduleLogDTO scheduleLog);

    /**
     * 查询日志
     *
     * @param id id
     * @return 日志对象
     */
    ScheduleLogDTO selectOne(Long id);

    /**
     * 分页查询
     *
     * @param scheduleLogParam 参数
     * @return page
     */
    Page<ScheduleLogDTO> listByPage(ScheduleLogParam scheduleLogParam);
}
