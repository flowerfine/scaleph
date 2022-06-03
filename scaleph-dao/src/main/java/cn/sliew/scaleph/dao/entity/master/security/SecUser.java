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

package cn.sliew.scaleph.dao.entity.master.security;

import java.util.Date;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sec_user")
@ApiModel(value = "SecUser对象", description = "用户基本信息表")
public class SecUser extends BaseDO {

    private static final long serialVersionUID = 2955806429097700570L;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "证件类型")
    private String idCardType;

    @ApiModelProperty(value = "证件号码")
    private String idCardNo;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "qq号码")
    private String qq;

    @ApiModelProperty(value = "微信号码")
    private String wechat;

    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "用户状态")
    private String userStatus;

    @ApiModelProperty(value = "用户简介")
    private String summary;

    @ApiModelProperty(value = "注册渠道")
    private String registerChannel;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;


}
