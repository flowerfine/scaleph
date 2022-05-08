package cn.sliew.breeze.service.di.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.DiJobLog;
import cn.sliew.breeze.dao.mapper.DiJobLogMapper;
import cn.sliew.breeze.service.convert.di.DiJobLogConvert;
import cn.sliew.breeze.service.di.DiJobLogService;
import cn.sliew.breeze.service.dto.di.DiJobLogDTO;
import cn.sliew.breeze.service.param.di.DiJobLogParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiJobLogServiceImpl implements DiJobLogService {

    @Autowired
    private DiJobLogMapper diJobLogMapper;

    @Override
    public int insert(DiJobLogDTO dto) {
        DiJobLog log = DiJobLogConvert.INSTANCE.toDo(dto);
        return this.diJobLogMapper.insert(log);
    }

    @Override
    public int update(DiJobLogDTO dto) {
        if (StrUtil.isEmpty(dto.getJobInstanceId())) {
            return 0;
        }
        DiJobLog log = DiJobLogConvert.INSTANCE.toDo(dto);
        return this.diJobLogMapper.update(log, new LambdaUpdateWrapper<DiJobLog>()
                .eq(DiJobLog::getJobInstanceId, log.getJobInstanceId())
        );
    }

    @Override
    public Page<DiJobLogDTO> listByPage(DiJobLogParam param) {
        Page<DiJobLogDTO> result = new Page<>();
        Page<DiJobLog> list = this.diJobLogMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<DiJobLog>()
                        .eq(StrUtil.isNotEmpty(param.getJobInstanceState()), DiJobLog::getJobInstanceState, param.getJobInstanceState())
                        .eq(param.getProjectId() != null, DiJobLog::getProjectId, param.getProjectId())
                        .like(StrUtil.isNotEmpty(param.getJobCode()), DiJobLog::getJobCode, param.getJobCode())
                        .ge(param.getStartTime() != null, DiJobLog::getStartTime, param.getStartTime())
                        .le(param.getEndTime() != null, DiJobLog::getEndTime, param.getEndTime())
        );
        List<DiJobLogDTO> dtoList = DiJobLogConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public DiJobLogDTO selectByJobInstanceId(String jobInstanceId) {
        DiJobLog log = this.diJobLogMapper.selectOne(
                new LambdaQueryWrapper<DiJobLog>()
                        .eq(DiJobLog::getJobInstanceId, jobInstanceId)
        );
        return DiJobLogConvert.INSTANCE.toDto(log);
    }

    @Override
    public List<DiJobLogDTO> listRunningJobInstance(String jobCode) {
        List<DiJobLog> list = this.diJobLogMapper.selectList(
                new LambdaQueryWrapper<DiJobLog>()
                        .eq(StrUtil.isNotBlank(jobCode), DiJobLog::getJobCode, jobCode)
                        .notIn(DiJobLog::getJobInstanceState, "FAILED", "CANCELED", "FINISHED")
        );
        return DiJobLogConvert.INSTANCE.toDto(list);
    }
}
