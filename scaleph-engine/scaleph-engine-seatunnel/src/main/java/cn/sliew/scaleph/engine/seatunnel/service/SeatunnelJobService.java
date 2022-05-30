package cn.sliew.scaleph.engine.seatunnel.service;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;

public interface SeatunnelJobService {

    void submit(DiJobRunVO jobRunParam);

    void schedule(DiJobDTO diJobDTO);

    void cancel(Long jobId);
}
