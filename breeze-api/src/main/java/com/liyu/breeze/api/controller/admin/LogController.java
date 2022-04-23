package com.liyu.breeze.api.controller.admin;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyu.breeze.api.annotation.Logging;
import com.liyu.breeze.api.util.SecurityUtil;
import com.liyu.breeze.service.admin.ActionLogService;
import com.liyu.breeze.service.admin.LoginLogService;
import com.liyu.breeze.service.dto.admin.LogLoginDTO;
import com.liyu.breeze.service.param.admin.LoginLogParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
    private LoginLogService loginLogService;
    @Autowired
    private ActionLogService actionLogService;

    @Logging
    @GetMapping(path = "login")
    @ApiOperation(value = "查询用户近30天的登录日志", notes = "查询用户近30天的登录日志")
    public ResponseEntity<Page<LogLoginDTO>> listLoginLogNearlyOneMonth(LoginLogParam param) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!StrUtil.isEmpty(userName)) {
            param.setUserName(userName);
            param.setLoginTime(DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), -30));
            Page<LogLoginDTO> result = this.loginLogService.listByPage(param);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
}

