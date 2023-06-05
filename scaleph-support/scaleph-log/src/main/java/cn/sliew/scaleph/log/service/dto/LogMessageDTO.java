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

package cn.sliew.scaleph.log.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 站内信表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "站内信", description = "站内信日志信息")
public class LogMessageDTO extends BaseDTO {

    private static final long serialVersionUID = -4802816346373359731L;
    @NotBlank
    @Length(min = 1, max = 100)
    @ApiModelProperty(value = "标题")
    private String title;

    @NotNull
    @ApiModelProperty(value = "消息类型")
    private DictVO messageType;

    @NotBlank
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "收件人")
    private String receiver;

    @NotBlank
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "发送人")
    private String sender;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "是否已读")
    private DictVO isRead;

    @ApiModelProperty(value = "是否删除")
    private DictVO isDelete;


}
