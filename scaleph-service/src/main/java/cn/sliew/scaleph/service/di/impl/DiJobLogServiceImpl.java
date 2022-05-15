package cn.sliew.scaleph.service.di.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobLogMapper;
import cn.sliew.scaleph.service.convert.di.DiJobLogConvert;
import cn.sliew.scaleph.service.di.DiJobLogService;
import cn.sliew.scaleph.service.dto.di.DiJobLogDTO;
import cn.sliew.scaleph.service.param.di.DiJobLogParam;
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
        Page<DiJobLogDTO> result = new Page<>(param.getCurrent(), param.getPageSize());
        result.setOrders(param.buildSortItems());
        Page<DiJobLog> list = this.diJobLogMapper.selectPage(result, param.toDo(), param.getJobType()
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
