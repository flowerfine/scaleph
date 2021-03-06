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

import cn.hutool.extra.servlet.ServletUtil;
import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.security.OnlineUserService;
import cn.sliew.scaleph.api.security.TokenProvider;
import cn.sliew.scaleph.api.security.UserDetailInfo;
import cn.sliew.scaleph.api.util.SecurityUtil;
import cn.sliew.scaleph.api.vo.*;
import cn.sliew.scaleph.cache.util.RedisUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.RegisterChannelEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.enums.UserStatusEnum;
import cn.sliew.scaleph.mail.service.EmailService;
import cn.sliew.scaleph.security.service.SecRoleService;
import cn.sliew.scaleph.security.service.SecUserActiveService;
import cn.sliew.scaleph.security.service.SecUserRoleService;
import cn.sliew.scaleph.security.service.SecUserService;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.dto.SecUserActiveDTO;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import cn.sliew.scaleph.security.service.dto.SecUserRoleDTO;
import cn.sliew.scaleph.security.service.param.SecUserParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ????????????????????? ???????????????
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api")
@Api(tags = "????????????-????????????")
public class SecUserController {

    @Value("${app.name}")
    private String appName;
    @Value("${app.host}")
    private String appHost;

    @Autowired
    private SecUserService secUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SecRoleService secRoleService;
    @Autowired
    private SecUserRoleService secUserRoleService;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private SecUserActiveService secUserActiveService;

    /**
     * ????????????????????????
     * ????????????
     *
     * @param loginUser ????????????
     * @return ?????????????????????token
     */

    @AnonymousAccess
    @PostMapping(path = "/user/login")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    public ResponseEntity<ResponseVO> login(@Validated @RequestBody LoginInfoVO loginUser,
                                            HttpServletRequest request) {
        //???????????????
        String authCode = (String) redisUtil.get(loginUser.getUuid());
        redisUtil.delKeys(loginUser.getUuid());
        if (!StringUtils.isEmpty(authCode) && authCode.equalsIgnoreCase(loginUser.getAuthCode())) {
            try {
                //?????????????????????
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser.getUserName(),
                                loginUser.getPassword());
                //spring security????????????userDetailsService?????????????????????????????????????????????????????????Authentication???????????????????????????SecurityContext???
                Authentication authentication =
                        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //??????token ??????uuid??????token
                String token = tokenProvider.createToken();
                final UserDetailInfo userInfo = (UserDetailInfo) authentication.getPrincipal();
                userInfo.setLoginTime(new Date());
                userInfo.setLoginIpAddress(ServletUtil.getClientIP(request));
                userInfo.setRemember(loginUser.getRemember());
                //??????????????????????????????????????????redis onlineuser???
                List<SecRoleDTO> roles = secUserService.getAllPrivilegeByUserName(userInfo.getUsername());
                userInfo.getUser().setRoles(roles);
                //???????????????redis???
                onlineUserService.insert(userInfo, token);
                //??????????????????token
                ResponseVO info = ResponseVO.sucess();
                info.setData(token);
                return new ResponseEntity<>(info, HttpStatus.OK);
            } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
                return new ResponseEntity<>(
                        ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                                I18nUtil.get("response.error.login.password"),
                                ErrorShowTypeEnum.ERROR_MESSAGE), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.authCode"), ErrorShowTypeEnum.ERROR_MESSAGE),
                    HttpStatus.OK);
        }
    }

    @AnonymousAccess
    @PostMapping(path = "/user/logout")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    public ResponseEntity<ResponseVO> logout(String token) {
        if (token != null) {
            this.onlineUserService.logoutByToken(token);
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @AnonymousAccess
    @PostMapping(path = "/user/passwd/edit")
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
                return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
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
     * ?????????????????????
     *
     * @param email ????????????
     * @return ResponseVO
     */
    @Logging
    @GetMapping(path = "/user/email/getAuth")
    @ApiOperation(value = "?????????????????????", notes = "???????????????????????????????????????????????????")
    public ResponseEntity<ResponseVO> sendActiveEmail(@Email String email) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.hasText(userName)) {
            SecUserActiveDTO activeDTO = new SecUserActiveDTO();
            activeDTO.setUserName(userName);
            long time = System.currentTimeMillis() + 1000 * 60 * 10;
            activeDTO.setExpiryTime(time);
            activeDTO.setActiveCode(randomPasswordGenerate(6));
            this.secUserActiveService.insert(activeDTO);
            String subject = appName + "????????????";
            String html = "<html><body><p>" +
                    "??????????????????" + userName +
                    "<br/><br/>?????????????????????/?????????????????????<br/><h3>" + activeDTO.getActiveCode() +
                    "</h3><br/> ??????:?????????????????????10????????????????????????????????????????????????" +
                    "</p></body></html>";
            String[] sendTo = {email};
            emailService.sendHtmlEmail(sendTo, subject, html);
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * ????????????????????????????????????
     *
     * @param authCode ?????????
     * @return ResponseVO
     */
    @Logging
    @GetMapping(path = "/user/email/auth")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseVO> getEmailAuthCode(@NotNull String authCode,
                                                       @Email String email) {
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
                user.setUserStatus(
                        DictVO.toVO(DictConstants.USER_STATUS, UserStatusEnum.BIND_EMAIL.getValue()));
                this.secUserActiveService.updateByUserAndCode(userActive);
                this.secUserService.updateByUserName(user);
                return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(
                    ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                            I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    /**
     * ??????token??????redis??????????????????
     *
     * @param token token
     * @return ???????????????????????????
     */
    @AnonymousAccess
    @GetMapping(path = "/user/get/{token}")
    @ApiOperation(value = "??????????????????", notes = "??????token??????????????????????????????")
    public ResponseEntity<ResponseVO> getOnlineUserInfo(
            @PathVariable(value = "token") String token) {
        OnlineUserVO onlineUser = this.onlineUserService.getAllPrivilegeByToken(token);
        ResponseVO info = ResponseVO.sucess();
        info.setData(onlineUser);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    /**
     * ????????????
     *
     * @param registerInfo       ??????????????????
     * @param httpServletRequest HttpServletRequest
     * @return OperateInfo
     */
    @Logging
    @AnonymousAccess
    @PostMapping(path = "/user/register")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseVO> register(@Validated @RequestBody RegisterInfoVO registerInfo,
                                               HttpServletRequest httpServletRequest) {
        //???????????????????????????
        String authCode = (String) redisUtil.get(registerInfo.getUuid());
        redisUtil.delKeys(registerInfo.getUuid());
        if (StringUtils.hasText(authCode) && authCode.equalsIgnoreCase(registerInfo.getAuthCode())) {
            //????????????????????????????????????
            if (registerInfo.getPassword().equals(registerInfo.getConfirmPassword())) {
                Date date = new Date();
                SecUserDTO secUserDTO = new SecUserDTO();
                secUserDTO.setUserName(registerInfo.getUserName().toLowerCase());
                secUserDTO.setEmail(registerInfo.getEmail().toLowerCase());
                String password = passwordEncoder.encode(registerInfo.getPassword());
                secUserDTO.setPassword(password);
                secUserDTO.setUserStatus(
                        DictVO.toVO(DictConstants.USER_STATUS, UserStatusEnum.UNBIND_EMAIL.getValue()));
                secUserDTO.setRegisterChannel(DictVO.toVO(DictConstants.REGISTER_CHANNEL,
                        RegisterChannelEnum.REGISTER.getValue()));
                secUserDTO.setRegisterTime(date);
                //???????????????ip??????
                String ipAddress = ServletUtil.getClientIP(httpServletRequest);
                secUserDTO.setRegisterIp(ipAddress);
                this.sendConfirmEmail(secUserDTO, null);
                this.secUserService.insert(secUserDTO);
                //????????????????????????
                SecUserDTO userInfo = this.secUserService.selectOne(secUserDTO.getUserName());
                SecRoleDTO secRoleDTO = secRoleService.selectOne(Constants.ROLE_NORMAL);
                SecUserRoleDTO secUserRoleDTO = new SecUserRoleDTO();
                secUserRoleDTO.setUserId(userInfo.getId());
                secUserRoleDTO.setRoleId(secRoleDTO.getId());
                secUserRoleDTO.setCreateTime(date);
                secUserRoleDTO.setUpdateTime(date);
                this.secUserRoleService.insert(secUserRoleDTO);
                return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
            } else {
                //??????????????????????????????????????????????????????
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
    @PostMapping(path = "/admin/user")
    @ApiOperation(value = "????????????", notes = "????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_ADD)")
    public ResponseEntity<ResponseVO> addUser(@Validated @RequestBody SecUserDTO secUserDTO,
                                              HttpServletRequest httpServletRequest) {
        Date date = new Date();
        secUserDTO.setRegisterTime(date);
        String randomPassword = this.randomPasswordGenerate(10);
        secUserDTO.setPassword(this.passwordEncoder.encode(randomPassword));
        secUserDTO.setUserStatus(
                DictVO.toVO(DictConstants.USER_STATUS, UserStatusEnum.UNBIND_EMAIL.getValue()));
        secUserDTO.setRegisterChannel(DictVO.toVO(DictConstants.REGISTER_CHANNEL,
                RegisterChannelEnum.BACKGROUND_IMPORT.getValue()));
        String ipAddress = ServletUtil.getClientIP(httpServletRequest);
        secUserDTO.setRegisterIp(ipAddress);
        this.secUserService.insert(secUserDTO);
        this.sendConfirmEmail(secUserDTO, randomPassword);
        //????????????????????????
        SecUserDTO userInfo = this.secUserService.selectOne(secUserDTO.getUserName());
        SecRoleDTO secRoleDTO = secRoleService.selectOne(Constants.ROLE_NORMAL);
        SecUserRoleDTO secUserRoleDTO = new SecUserRoleDTO();
        secUserRoleDTO.setUserId(userInfo.getId());
        secUserRoleDTO.setRoleId(secRoleDTO.getId());
        secUserRoleDTO.setCreateTime(date);
        secUserRoleDTO.setUpdateTime(date);
        this.secUserRoleService.insert(secUserRoleDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }


    @Logging
    @PutMapping(path = "/admin/user")
    @ApiOperation(value = "????????????", notes = "????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_EDIT)")
    public ResponseEntity<ResponseVO> editUser(@Validated @RequestBody SecUserDTO secUserDTO) {
        this.secUserService.update(secUserDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


    @Logging
    @DeleteMapping(path = "/admin/user/{id}")
    @ApiOperation(value = "????????????", notes = "??????id????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_DELETE)")
    public ResponseEntity<ResponseVO> deleteUser(@PathVariable(value = "id") String id) {
        this.secUserService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/admin/user/batch")
    @ApiOperation(value = "??????????????????", notes = "??????id????????????????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchUser(@RequestBody Map<Integer, String> map) {
        this.secUserService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/admin/user")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).USER_SELECT)")
    public ResponseEntity<Page<SecUserDTO>> listUser(SecUserParam secUserParam) {
        Page<SecUserDTO> page = this.secUserService.listByPage(secUserParam);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * ??????????????????
     *
     * @param length ????????????
     * @return ??????
     */
    private String randomPasswordGenerate(int length) {
        //??????????????????
        char[][] pairs = {{'a', 'z'}, {'A', 'Z'}, {'0', '9'}};
        RandomStringGenerator rsg = new RandomStringGenerator.Builder().withinRange(pairs).build();
        return rsg.generate(length);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param secUserDTO ????????????
     */
    private void sendConfirmEmail(SecUserDTO secUserDTO, String password) {
        String subject = appName + "????????????";
        String html = "<html><body><p>" +
                "??????????????????<br/> ???????????????" + appName + "?????????" + secUserDTO.getUserName() + "?????????";
        if (!StringUtils.isEmpty(password)) {
            html = html + "?????????????????????" + password;
        }
        html = html + "???<br/> ?????????????????????????????????" +
                "<br/> </p></body></html>";

        String[] sendTo = {secUserDTO.getEmail()};
        this.emailService.sendHtmlEmail(sendTo, subject, html);
    }

    /**
     * ???????????????????????????
     *
     * @param userName ?????????
     * @return true/false
     */
    @Logging
    @AnonymousAccess
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????????????????")
    @GetMapping(path = "/user/validation/userName")
    public ResponseEntity<Boolean> isUserNameExists(String userName) {
        SecUserDTO user = this.secUserService.selectOne(userName);
        return new ResponseEntity<>(user == null, HttpStatus.OK);
    }

    /**
     * ??????????????????????????????
     *
     * @param email ????????????
     * @return true/false
     */
    @Logging
    @AnonymousAccess
    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????????????????")
    @GetMapping(path = "/user/validation/email")
    public ResponseEntity<Boolean> isEmailExists(String email) {
        SecUserDTO user = this.secUserService.selectByEmail(email);
        return new ResponseEntity<>(user == null, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/user/info")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????")
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
     * ???????????????????????????????????????
     *
     * @param userName  ?????????
     * @param roleId    ??????id
     * @param direction 1:target 0:source
     * @return user list
     */
    @Logging
    @PostMapping(path = "/user/role")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).ROLE_GRANT)")
    public ResponseEntity<List<TransferVO>> listUserByUserAndRole(String userName, Long roleId,
                                                                  String direction) {
        List<TransferVO> result = new ArrayList<>();
        List<SecUserDTO> userList = this.secUserService.listByRole(roleId, userName, direction);
        userList.forEach(d -> {
            result.add(new TransferVO(String.valueOf(d.getId()), d.getUserName()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param userName  ?????????
     * @param deptId    dept id
     * @param direction 1:target 0:source
     * @return user list
     */
    @Logging
    @PostMapping(path = "/user/dept")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????????????????")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DEPT_GRANT)")
    public ResponseEntity<List<TransferVO>> listUserByUserAndDept(String userName, Long deptId,
                                                                  String direction) {
        List<TransferVO> result = new ArrayList<>();
        List<SecUserDTO> userList = this.secUserService.listByDept(deptId, userName, direction);
        userList.forEach(d -> {
            result.add(new TransferVO(String.valueOf(d.getId()), d.getUserName()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

