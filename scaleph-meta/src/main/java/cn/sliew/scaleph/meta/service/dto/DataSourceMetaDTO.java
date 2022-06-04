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

package cn.sliew.scaleph.meta.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author lenovo
 */
@Data
@ApiModel(value = "数据源信息", description = "数据源信息")
@EqualsAndHashCode(callSuper = true)
public class DataSourceMetaDTO extends BaseDTO {
    private static final long serialVersionUID = 6680513812051305417L;
    /**
     * 数据源名称
     */
    @NotBlank
    @Length(max = 50)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty("数据源名称")
    private String dataSourceName;
    /**
     * 数据源类型
     */
    @NotNull
    @ApiModelProperty("数据源类型")
    private DictVO dataSourceType;
    /**
     * 连接类型
     */
    @NotNull
    @ApiModelProperty("数据源连接类型")
    private DictVO connectionType;
    /**
     * 主机地址
     */

    @Length(max = 240)
    @ApiModelProperty("主机地址")
    private String hostName;
    /**
     * 数据库名称
     */
    @Length(max = 50)
    @ApiModelProperty("数据库名称")
    private String databaseName;
    /**
     * 端口号
     */
    @ApiModelProperty("端口号")
    private Integer port;
    /**
     * 用户名
     */
    @Length(max = 60)
    @ApiModelProperty("用户名")
    private String userName;
    /**
     * 密码
     */
    @Length(max = 256)
    @ApiModelProperty("密码")
    private String password;
    /**
     * 备注
     */
    @Length(max = 240)
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("数据源常规属性")
    private String generalProps;
    /**
     * 数据源连接属性
     */
    @ApiModelProperty("数据源JDBC属性")
    private String jdbcProps;

    /**
     * 连接池连接属性
     */
    @ApiModelProperty("数据源池连接属性")
    private String poolProps;
    /**
     * 是否改变了密码
     */
    @ApiModelProperty("是否改变了密码")
    private Boolean passwdChanged;

}
