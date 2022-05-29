package cn.sliew.scaleph.api.controller.admin;


import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.SecurityUtil;
import cn.sliew.scaleph.log.service.LogActionService;
import cn.sliew.scaleph.log.service.LogLoginService;
import cn.sliew.scaleph.log.service.dto.LogLoginDTO;
import cn.sliew.scaleph.log.service.param.LogLoginParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户操作日志 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/log")
@Api(tags = "系统管理-日志管理")
public class LogController {

    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogActionService logActionService;

    @Logging
    @GetMapping(path = "login")
    @ApiOperation(value = "查询用户近30天的登录日志", notes = "查询用户近30天的登录日志")
    public ResponseEntity<Page<LogLoginDTO>> listLoginLogNearlyOneMonth(LogLoginParam param) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!StrUtil.isEmpty(userName)) {
            param.setUserName(userName);
            param.setLoginTime(DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), -30));
            Page<LogLoginDTO> result = this.logLoginService.listByPage(param);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
}

