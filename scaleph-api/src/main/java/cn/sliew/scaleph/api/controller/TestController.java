package cn.sliew.scaleph.api.controller;

import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试模块")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private SeatunnelStorageService seatunnelStorageService;
    @Autowired
    private SeatunnelJobService seatunnelJobService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;

    @PostMapping(path = "/downloadFlinkRelease")
    @ApiOperation("下载 flink release")
    public ResponseEntity<ResponseVO> downloadFlinkRelease() {
        seatunnelStorageService.downloadFlinkRelease(FlinkRelease.V_1_13_6);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @GetMapping(path = "/buildSeatunnelConfig")
    @ApiOperation("生成 seatunnel config")
    public ResponseEntity<ResponseVO> buildSeatunnelConfig(@RequestParam("jobId") Long jobId) {
        final DiJobDTO diJobDTO = seatunnelJobService.queryJobInfo(jobId);
        final String config = seatunnelConfigService.buildConfig(diJobDTO);
        System.out.println(config);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
