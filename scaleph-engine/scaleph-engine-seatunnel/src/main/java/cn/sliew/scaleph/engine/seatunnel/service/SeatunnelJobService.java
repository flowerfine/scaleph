package cn.sliew.scaleph.engine.seatunnel.service;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;

public interface SeatunnelJobService {

    void submit(DiJobDTO diJobDTO);

    void schedule(DiJobDTO diJobDTO);

    void cancel(Long jobId);
}
