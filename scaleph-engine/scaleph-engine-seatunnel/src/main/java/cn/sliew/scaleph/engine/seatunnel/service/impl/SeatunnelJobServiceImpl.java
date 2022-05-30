package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeatunnelJobServiceImpl implements SeatunnelJobService {

    @Override
    public void submit(DiJobDTO diJobDTO) {

    }

    @Override
    public void schedule(DiJobDTO diJobDTO) {

    }

    @Override
    public void cancel(Long jobId) {

    }
}
