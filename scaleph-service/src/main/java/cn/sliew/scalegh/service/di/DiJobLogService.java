package cn.sliew.scalegh.service.di;

import cn.sliew.scalegh.service.dto.di.DiJobLogDTO;
import cn.sliew.scalegh.service.param.di.DiJobLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

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
}
