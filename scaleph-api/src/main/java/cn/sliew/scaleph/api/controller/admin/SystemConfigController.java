package cn.sliew.scaleph.api.controller.admin;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.I18nUtil;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.mail.service.EmailService;
import cn.sliew.scaleph.mail.service.vo.EmailConfigVO;
import cn.sliew.scaleph.service.vo.BasicConfigVO;
import cn.sliew.scaleph.system.service.SystemConfigService;
import cn.sliew.scaleph.system.service.dto.SystemConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * @author gleiyu
 */
@RestController
@RequestMapping("/api/admin/config")
@Api(tags = "系统管理-系统配置")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private EmailService emailService;

    @Logging
    @PutMapping(path = "email")
    @ApiOperation(value = "设置系统邮箱", notes = "设置系统邮箱")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseVO> configEmail(@Validated @RequestBody EmailConfigVO emailConfig) {
        String password = emailConfig.getPassword();
        emailConfig.setPassword(Base64.encode(password));
        this.systemConfigService.deleteByCode(Constants.CFG_EMAIL_CODE);
        SystemConfigDTO systemConfig = new SystemConfigDTO();
        systemConfig.setCfgCode(Constants.CFG_EMAIL_CODE);
        systemConfig.setCfgValue(JSONUtil.toJsonStr(emailConfig));
        this.systemConfigService.insert(systemConfig);
        this.emailService.configEmail(emailConfig);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "email")
    @ApiOperation(value = "查询系统邮箱", notes = "查询系统邮箱")
    public ResponseEntity<EmailConfigVO> showEmail() {
        SystemConfigDTO systemConfig = this.systemConfigService.selectByCode(Constants.CFG_EMAIL_CODE);
        if (systemConfig != null) {
            EmailConfigVO config = JSONUtil.toBean(systemConfig.getCfgValue(), EmailConfigVO.class);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmailConfigVO(), HttpStatus.OK);
        }
    }


    @Logging
    @PutMapping(path = "basic")
    @ApiOperation(value = "设置基础配置", notes = "设置基础配置")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseVO> configBasic(@Validated @RequestBody BasicConfigVO basicConfig) {
        String seatunnelHome = basicConfig.getSeatunnelHome().replace("\\", "/");
        if (seatunnelHome.endsWith("/")) {
            seatunnelHome += "lib";
        } else {
            seatunnelHome += "/lib";
        }
        File libDir = new File(seatunnelHome);
        // remove JarFilter which it has been removed by later jdk release.
        File[] fileList = libDir.listFiles((dir, name) -> {
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jar") || lowerCaseName.endsWith(".zip");
        });
        boolean flag = false;
        if (fileList != null) {
            for (File file : fileList) {
                if ("seatunnel-core-flink.jar".equals(file.getName())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.setting.seatunnel"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.systemConfigService.deleteByCode(Constants.CFG_BASIC_CODE);
        SystemConfigDTO systemConfig = new SystemConfigDTO();
        systemConfig.setCfgCode(Constants.CFG_BASIC_CODE);
        systemConfig.setCfgValue(JSONUtil.toJsonStr(basicConfig));
        this.systemConfigService.insert(systemConfig);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "basic")
    @ApiOperation(value = "查询基础设置", notes = "查询基础设置")
    public ResponseEntity<BasicConfigVO> showBasci() {
        SystemConfigDTO systemConfig = this.systemConfigService.selectByCode(Constants.CFG_BASIC_CODE);
        if (systemConfig != null) {
            BasicConfigVO config = JSONUtil.toBean(systemConfig.getCfgValue(), BasicConfigVO.class);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BasicConfigVO(), HttpStatus.OK);
        }
    }


}
