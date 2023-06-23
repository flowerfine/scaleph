/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.api.controller.admin;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.mail.service.EmailService;
import cn.sliew.scaleph.mail.service.vo.EmailConfigVO;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.system.service.SysConfigService;
import cn.sliew.scaleph.system.service.dto.SysConfigDTO;
import cn.sliew.scaleph.system.service.vo.BasicConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;

import static cn.sliew.scaleph.common.constant.Constants.CODEC_STR_PREFIX;

/**
 * @author gleiyu
 */
@RestController
@RequestMapping("/api/admin/config")
@Tag(name = "系统管理-系统配置")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private EmailService emailService;

    @Logging
    @PutMapping("email")
    @Operation(summary = "设置系统邮箱", description = "设置系统邮箱")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public ResponseEntity<ResponseVO> configEmail(@Validated @RequestBody EmailConfigVO emailConfig) {
        String password = emailConfig.getPassword();
        if (!password.startsWith(CODEC_STR_PREFIX)) {
            emailConfig.setPassword(CODEC_STR_PREFIX + CodecUtil.encodeToBase64(password));
        } else {
            emailConfig.setPassword(password);
        }
        this.sysConfigService.deleteByCode(Constants.CFG_EMAIL_CODE);
        SysConfigDTO sysConfig = new SysConfigDTO();
        sysConfig.setCfgCode(Constants.CFG_EMAIL_CODE);
        sysConfig.setCfgValue(JacksonUtil.toJsonString(emailConfig));
        this.sysConfigService.insert(sysConfig);
        this.emailService.configEmail(emailConfig);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("email")
    @Operation(summary = "查询系统邮箱", description = "查询系统邮箱")
    public ResponseEntity<EmailConfigVO> showEmail() {
        SysConfigDTO sysConfig =
                this.sysConfigService.selectByCode(Constants.CFG_EMAIL_CODE);
        if (sysConfig != null) {
            EmailConfigVO config = JacksonUtil.parseJsonString(sysConfig.getCfgValue(), EmailConfigVO.class);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmailConfigVO(), HttpStatus.OK);
        }
    }


    @Logging
    @PutMapping("basic")
    @Operation(summary = "设置基础配置", description = "设置基础配置")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
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
                    I18nUtil.get("response.error.setting.seatunnel"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
        this.sysConfigService.deleteByCode(Constants.CFG_BASIC_CODE);
        SysConfigDTO sysConfig = new SysConfigDTO();
        sysConfig.setCfgCode(Constants.CFG_BASIC_CODE);
        sysConfig.setCfgValue(JacksonUtil.toJsonString(basicConfig));
        this.sysConfigService.insert(sysConfig);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("basic")
    @Operation(summary = "查询基础设置", description = "查询基础设置")
    public ResponseEntity<BasicConfigVO> showBasic() {
        SysConfigDTO sysConfig =
                this.sysConfigService.selectByCode(Constants.CFG_BASIC_CODE);
        if (sysConfig != null) {
            BasicConfigVO config = JacksonUtil.parseJsonString(sysConfig.getCfgValue(), BasicConfigVO.class);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BasicConfigVO(), HttpStatus.OK);
        }
    }

}
