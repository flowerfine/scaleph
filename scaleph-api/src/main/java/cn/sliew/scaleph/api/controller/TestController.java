package cn.sliew.scaleph.api.controller;

import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.engine.seatunnel.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试模块")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private SeatunnelStorageService seatunnelStorageService;

    @PostMapping(path = "/downloadFlinkRelease")
    @ApiOperation("下载 flink release")
    public ResponseEntity<ResponseVO> downloadFlinkRelease() {
        seatunnelStorageService.downloadFlinkRelease(FlinkRelease.V_1_13_6);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
