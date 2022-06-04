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

package cn.sliew.scaleph.security.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户信息", description = "用户基本信息表")
public class SecUserDTO extends BaseDTO {

    private static final long serialVersionUID = -1821402534416659344L;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Length(max = 50)
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Email
    @NotNull
    @ApiModelProperty(value = "邮箱")
    private String email;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @Length(max = 30)
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "证件类型")
    private DictVO idCardType;

    @Length(max = 18)
    @ApiModelProperty(value = "证件号码")
    private String idCardNo;

    @ApiModelProperty(value = "性别")
    private DictVO gender;

    @ApiModelProperty(value = "民族")
    private DictVO nation;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @Length(max = 15)
    @ApiModelProperty(value = "qq号码")
    private String qq;

    @Length(max = 60)
    @ApiModelProperty(value = "微信号码")
    private String wechat;

    @Length(max = 11)
    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "用户状态")
    private DictVO userStatus;

    @Length(max = 500)
    @ApiModelProperty(value = "用户简介")
    private String summary;

    @ApiModelProperty(value = "注册渠道")
    private DictVO registerChannel;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "角色列表")
    private List<SecRoleDTO> roles;
}
