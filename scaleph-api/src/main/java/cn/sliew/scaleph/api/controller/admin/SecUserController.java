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

import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.RegisterInfoVO;
import cn.sliew.scaleph.api.vo.TransferVO;
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.dict.security.UserStatus;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.mail.service.EmailService;
import cn.sliew.scaleph.security.service.*;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserActiveDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.dto.SecUserRoleDTO;
import cn.sliew.scaleph.security.service.param.SecLoginParam;
import cn.sliew.scaleph.security.service.param.SecUserParam;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.security.vo.OnlineUserVO;
import cn.sliew.scaleph.security.web.OnlineUserService;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户基本信息表 前端控制器
 * </p>
 *
 * @author liyu
 */
//@RestController
@RequestMapping("/api")
@Tag(name = "系统管理-用户管理")
public class SecUserController {

    @Value("${app.name}")
    private String appName;
    @Value("${app.host}")
    private String appHost;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SecUserService secUserService;
    @Autowired
    private SecRoleService secRoleService;
    @Autowired
    private SecUserRoleService secUserRoleService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private SecUserActiveService secUserActiveService;
    @Autowired
    private SecAuthenticateService secAuthenticateService;

    @AnonymousAccess
    @PostMapping("/user/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<ResponseVO> login(@Validated @RequestBody SecLoginParam param, HttpServletRequest request) {
        ResponseVO responseVO = secAuthenticateService.login(request, param);
        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }

    @AnonymousAccess
    @PostMapping("/user/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public ResponseEntity<ResponseVO> logout(String token) {
        secAuthenticateService.logout(token);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @AnonymousAccess
    @PostMapping("/user/passwd/edit")
    public ResponseEntity<ResponseVO> editPassword(@NotNull String oldPassword,
                                                   @NotNull String password,
                                                   @NotNull String confirmPassword) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.isEmpty(userName)) {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }

        if (password.equals(confirmPassword)) {
            SecUserDTO user = this.secUserService.selectOne(userName);
            if (this.passwordEncoder.matches(oldPassword, user.getPassword())) {
                SecUserDTO secUserDTO = new SecUserDTO();
                secUserDTO.setId(user.getId());
                secUserDTO.setPassword(this.passwordEncoder.encode(password));
                this.secUserService.update(secUserDTO);
                return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        ResponseVO.error(I18nUtil.get("response.error.oldPassword")),
                        HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(
                    ResponseVO.error(I18nUtil.get("response.error.notSamePassword")),
                    HttpStatus.OK);
        }
    }

    /**
     * 发送激活码邮件
     *
     * @param email 邮箱地址
     * @return ResponseVO
     */
    @Logging
    @GetMapping("/user/email/getAuth")
    @Operation(summary = "获取邮箱验证码", description = "用户登录后，绑定获取邮箱绑定验证码")
    public ResponseEntity<ResponseVO> sendActiveEmail(@Email String email) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.hasText(userName)) {
            SecUserActiveDTO activeDTO = new SecUserActiveDTO();
            activeDTO.setUserName(userName);
            long time = System.currentTimeMillis() + 1000 * 60 * 10;
            activeDTO.setExpiryTime(time);
            activeDTO.setActiveCode(randomPasswordGenerate(6));
            this.secUserActiveService.insert(activeDTO);
            String subject = appName + "邮箱绑定";
            String html = "<html><body><p>" +
                    "尊敬的用户：" + userName +
                    "<br/><br/>您本次邮箱变更/绑定的验证码为<br/><h3>" + activeDTO.getActiveCode() +
                    "</h3><br/> 注意:请您在收到邮件10分钟内使用，否则该验证码将会失效" +
                    "</p></body></html>";
            String[] sendTo = {email};
            emailService.sendHtmlEmail(sendTo, subject, html);
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    /**
     * 用户登录后，绑定邮箱地址
     *
     * @param authCode 验证码
     * @return ResponseVO
     */
    @Logging
    @GetMapping("/user/email/auth")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public ResponseEntity<ResponseVO> getEmailAuthCode(@NotNull String authCode, @Email String email) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.hasText(userName)) {
            SecUserActiveDTO userActive = this.secUserActiveService.selectOne(userName, authCode);
            if (userActive == null || System.currentTimeMillis() > userActive.getExpiryTime()) {
                return new ResponseEntity<>(
                        ResponseVO.error(I18nUtil.get("response.error.authCode.expired")),
                        HttpStatus.OK);
            } else {
                SecUserDTO user = new SecUserDTO();
                user.setUserName(userName);
                user.setEmail(email);
                user.setStatus(UserStatus.ENABLED);
                this.secUserActiveService.updateByUserAndCode(userActive);
                this.secUserService.updateByUserName(user);
                return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    /**
     * 根据token获取redis中的用户信息
     *
     * @param token token
     * @return 用户及权限角色信息
     */
    @AnonymousAccess
    @GetMapping("/user/get/{token}")
    @Operation(summary = "查询用户权限", description = "根据token信息查询用户所有权限")
    public ResponseEntity<ResponseVO> getOnlineUserInfo(@PathVariable(value = "token") String token) {
        OnlineUserVO onlineUser = this.onlineUserService.getAllPrivilegeByToken(token);
        ResponseVO info = ResponseVO.success();
        info.setData(onlineUser);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    /**
     * 用户注册
     *
     * @param registerInfo 用户注册信息
     * @return OperateInfo
     */
    @Logging
    @AnonymousAccess
    @PostMapping("/user/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public ResponseEntity<ResponseVO> register(@Validated @RequestBody RegisterInfoVO registerInfo) {
        //校验验证码是否一致
        String authCode = (String) redisUtil.get(registerInfo.getUuid());
        redisUtil.delKeys(registerInfo.getUuid());
        if (StringUtils.hasText(authCode) && authCode.equalsIgnoreCase(registerInfo.getAuthCode())) {
            //校验两次输入密码是否一致
            if (registerInfo.getPassword().equals(registerInfo.getConfirmPassword())) {
                Date date = new Date();
                SecUserDTO secUserDTO = new SecUserDTO();
                secUserDTO.setUserName(registerInfo.getUserName().toLowerCase());
                secUserDTO.setEmail(registerInfo.getEmail().toLowerCase());
                String password = passwordEncoder.encode(registerInfo.getPassword());
                secUserDTO.setPassword(password);
                secUserDTO.setStatus(UserStatus.ENABLED);
                this.sendConfirmEmail(secUserDTO, null);
                this.secUserService.insert(secUserDTO);
                //授权普通用户角色
                SecUserDTO userInfo = this.secUserService.selectOne(secUserDTO.getUserName());
                SecRoleDTO secRoleDTO = secRoleService.selectOne(Constants.ROLE_NORMAL);
                SecUserRoleDTO secUserRoleDTO = new SecUserRoleDTO();
                secUserRoleDTO.setUserId(userInfo.getId());
                secUserRoleDTO.setRoleId(secRoleDTO.getId());
                secUserRoleDTO.setCreateTime(date);
                secUserRoleDTO.setUpdateTime(date);
                this.secUserRoleService.insert(secUserRoleDTO);
                return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
            } else {
                //前台有验证提示，此处只做返回，不展示
                return new ResponseEntity<>(
                        ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                                I18nUtil.get("response.error"), ErrorShowTypeEnum.SILENT), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.authCode"), ErrorShowTypeEnum.ERROR_MESSAGE),
                    HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping("/admin/user")
    @Operation(summary = "新增用户", description = "新增用户")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_ADD)")
    public ResponseEntity<ResponseVO> addUser(@Validated @RequestBody SecUserDTO secUserDTO) {
        Date date = new Date();
        String randomPassword = RandomStringUtils.randomAlphanumeric(10);
        secUserDTO.setPassword(this.passwordEncoder.encode(randomPassword));
        secUserDTO.setStatus(UserStatus.ENABLED);
        this.secUserService.insert(secUserDTO);
        this.sendConfirmEmail(secUserDTO, randomPassword);
        //授权普通用户角色
        SecUserDTO userInfo = this.secUserService.selectOne(secUserDTO.getUserName());
        SecRoleDTO secRoleDTO = secRoleService.selectOne(Constants.ROLE_NORMAL);
        SecUserRoleDTO secUserRoleDTO = new SecUserRoleDTO();
        secUserRoleDTO.setUserId(userInfo.getId());
        secUserRoleDTO.setRoleId(secRoleDTO.getId());
        secUserRoleDTO.setCreateTime(date);
        secUserRoleDTO.setUpdateTime(date);
        this.secUserRoleService.insert(secUserRoleDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }


    @Logging
    @PutMapping("/admin/user")
    @Operation(summary = "修改用户", description = "修改用户")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_EDIT)")
    public ResponseEntity<ResponseVO> editUser(@Validated @RequestBody SecUserDTO secUserDTO) {
        this.secUserService.update(secUserDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }


    @Logging
    @DeleteMapping("/admin/user/{id}")
    @Operation(summary = "删除用户", description = "根据id删除用户")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_DELETE)")
    public ResponseEntity<ResponseVO> deleteUser(@PathVariable("id") String id) {
        this.secUserService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("/admin/user/batch")
    @Operation(summary = "批量删除用户", description = "根据id列表批量删除用户")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchUser(@RequestBody Map<Integer, Integer> map) {
        this.secUserService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/admin/user")
    @Operation(summary = "分页查询用户", description = "分页查询用户")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_SELECT)")
    public ResponseEntity<Page<SecUserDTO>> listUser(SecUserParam secUserParam) {
        Page<SecUserDTO> page = this.secUserService.listByPage(secUserParam);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * 随机生成密码
     *
     * @param length 密码长度
     * @return 密码
     */
    private String randomPasswordGenerate(int length) {
        //随机生成密码
        char[][] pairs = {{'a', 'z'}, {'A', 'Z'}, {'0', '9'}};
        RandomStringGenerator rsg = new RandomStringGenerator.Builder().withinRange(pairs).build();
        return rsg.generate(length);
    }

    /**
     * 向用户邮箱发送邮件，确认用户注册信息
     *
     * @param secUserDTO 用户信息
     */
    private void sendConfirmEmail(SecUserDTO secUserDTO, String password) {
        String subject = appName + "注册确认";
        String html = "<html><body><p>" +
                "尊敬的用户：<br/> 感谢您注册" + appName + "，账号" + secUserDTO.getUserName() + "已开通";
        if (!StringUtils.isEmpty(password)) {
            html = html + "，初始密码为：" + password;
        }
        html = html + "。<br/> 登录后请及时修改密码。" +
                "<br/> </p></body></html>";

        String[] sendTo = {secUserDTO.getEmail()};
        this.emailService.sendHtmlEmail(sendTo, subject, html);
    }

    /**
     * 验证用户名是否存在
     *
     * @param userName 用户名
     * @return true/false
     */
    @Logging
    @AnonymousAccess
    @GetMapping("/user/validation/userName")
    @Operation(summary = "判断用户是否存在", description = "根据用户名，判断用户是否存在")
    public ResponseEntity<Boolean> isUserNameExists(String userName) {
        SecUserDTO user = this.secUserService.selectOne(userName);
        return new ResponseEntity<>(user == null, HttpStatus.OK);
    }

    /**
     * 验证用户邮箱是否存在
     *
     * @param email 邮箱地址
     * @return true/false
     */
    @Logging
    @AnonymousAccess
    @GetMapping("/user/validation/email")
    @Operation(summary = "判断邮箱是否存在", description = "根据邮箱，判断用户是否存在")
    public ResponseEntity<Boolean> isEmailExists(String email) {
        SecUserDTO user = this.secUserService.selectByEmail(email);
        return new ResponseEntity<>(user == null, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/user/info")
    @Operation(summary = "根据用户名查询用户信息", description = "根据用户名查询用户信息")
    public ResponseEntity<SecUserDTO> listUserByUserName() {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.hasText(userName)) {
            SecUserDTO userinfo = this.secUserService.selectOne(userName);
            return new ResponseEntity<>(userinfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SecUserDTO(), HttpStatus.OK);
        }
    }

    /**
     * 配合前端穿梭框查询用户列表
     *
     * @param userName  用户名
     * @param roleId    角色id
     * @param direction 1:target 0:source
     * @return user list
     */
    @Logging
    @PostMapping("/user/role")
    @Operation(summary = "查询角色下用户列表", description = "配合前端穿梭框查询用户列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<TransferVO>> listUserByUserAndRole(String userName, Long roleId, String direction) {
        List<TransferVO> result = new ArrayList<>();
        List<SecUserDTO> userList = this.secUserService.listByRole(roleId, userName, direction);
        userList.forEach(d -> {
            result.add(new TransferVO(String.valueOf(d.getId()), d.getUserName()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 配合前端穿梭框查询用户列表
     *
     * @param userName  用户名
     * @param deptId    dept id
     * @param direction 1:target 0:source
     * @return user list
     */
    @Logging
    @PostMapping("/user/dept")
    @Operation(summary = "查询部门下用户列表", description = "配合前端穿梭框查询用户列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DEPT_GRANT)")
    public ResponseEntity<List<TransferVO>> listUserByUserAndDept(String userName, Long deptId, String direction) {
        List<TransferVO> result = new ArrayList<>();
        List<SecUserDTO> userList = this.secUserService.listByDept(deptId, userName, direction);
        userList.forEach(d -> {
            result.add(new TransferVO(String.valueOf(d.getId()), d.getUserName()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
