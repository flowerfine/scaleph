package cn.sliew.scaleph.api.controller.opscenter;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.service.di.DiJobLogService;
import cn.sliew.scaleph.service.dto.di.DiJobLogDTO;
import cn.sliew.scaleph.service.param.di.DiJobLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "运维中心-任务日志")
@RestController
@RequestMapping(path = "/api/opscenter/log")
public class JobLogController {

    @Autowired
    private DiJobLogService diJobLogService;

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "查询周期任务日志", notes = "分页查询周期任务日志")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).OPSCENTER_BATCH_SELECT)")
    public ResponseEntity<Page<DiJobLogDTO>> listBatchJobLog(@RequestBody DiJobLogParam param) {
        Page<DiJobLogDTO> page = this.diJobLogService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/realtime")
    @ApiOperation(value = "查询实时任务日志", notes = "分页查询实时任务日志")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).OPSCENTER_REALTIME_SELECT)")
    public ResponseEntity<Page<DiJobLogDTO>> listRealtimeJobLog(@RequestBody DiJobLogParam param) {
        Page<DiJobLogDTO> page = this.diJobLogService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
