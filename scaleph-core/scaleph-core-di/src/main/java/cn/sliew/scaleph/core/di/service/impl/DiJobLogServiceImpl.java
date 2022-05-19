package cn.sliew.scaleph.core.di.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.enums.JobTypeEnum;
import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobLogMapper;
import cn.sliew.scaleph.core.di.service.convert.DiJobLogConvert;
import cn.sliew.scaleph.core.di.service.DiJobLogService;
import cn.sliew.scaleph.core.di.service.dto.DiJobLogDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobLogParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<DiJobLogDTO> listTop100BatchJob(Date startTime) {
        List<DiJobLog> list = this.diJobLogMapper.selectTopN(
                JobTypeEnum.BATCH.getValue(),
                100,
                startTime
        );
        return DiJobLogConvert.INSTANCE.toDto(list);
    }

    @Override
    public Map<String, String> groupRealtimeJobRuntimeStatus() {
        List<Map<String, Object>> list = this.diJobLogMapper.selectRealtimeJobRuntimeStatus(JobTypeEnum.REALTIME.getValue());
        Map<String, String> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (Map<String, Object> m : list) {
                String name = String.valueOf(m.get("name"));
                String value = String.valueOf(m.get("value"));
                map.put(name, value);
            }
        }
        return map;
    }
}
