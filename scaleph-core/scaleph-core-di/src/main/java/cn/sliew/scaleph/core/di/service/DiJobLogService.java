package cn.sliew.scaleph.core.di.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.dto.DiJobLogDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集成-作业运行日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-05-06
 */
public interface DiJobLogService {

    int insert(DiJobLogDTO dto);

    int update(DiJobLogDTO dto);

    Page<DiJobLogDTO> listByPage(DiJobLogParam param);

    DiJobLogDTO selectByJobInstanceId(String jobInstanceId);

    List<DiJobLogDTO> listRunningJobInstance(String jobCode);

    List<DiJobLogDTO> listTop100BatchJob(Date startTime);

    Map<String, String> groupRealtimeJobRuntimeStatus();
}
