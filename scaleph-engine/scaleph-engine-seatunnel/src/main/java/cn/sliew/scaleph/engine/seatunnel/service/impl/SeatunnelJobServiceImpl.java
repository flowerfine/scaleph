package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeatunnelJobServiceImpl implements SeatunnelJobService {

    @Autowired
    private DiJobService diJobService;

    @Override
    public void submit(DiJobRunVO jobRunParam) {
        // 执行任务和 flink 集群的绑定
        diJobService.update(jobRunParam.toDto());

    }

    @Override
    public void schedule(DiJobDTO diJobDTO) {

    }

    @Override
    public void cancel(Long jobId) {

    }
}
