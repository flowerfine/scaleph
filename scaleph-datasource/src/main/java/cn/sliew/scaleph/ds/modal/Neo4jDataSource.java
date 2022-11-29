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

package cn.sliew.scaleph.ds.modal;

import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.dto.DsTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Data
@EqualsAndHashCode(callSuper = true)
public class Neo4jDataSource extends AbstractDataSource {

    @NotBlank
    @ApiModelProperty("uri")
    private String uri;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("password")
    private String password;

    @ApiModelProperty("Bearer Token")
    private String bearerToken;

    @ApiModelProperty("Kerberos Ticket")
    private String kerberosTicket;

    @Override
    public DataSourceType getType() {
        return DataSourceType.NEO4J;
    }

    @Override
    public DsInfoDTO toDsInfo() {
        DsInfoDTO dto = BeanUtil.copy(this, new DsInfoDTO());
        DsTypeDTO dsType = new DsTypeDTO();
        dsType.setId(getDsTypeId());
        dsType.setType(getType());
        dto.setDsType(dsType);
        Map<String, Object> props = new HashMap<>();
        props.put("uri", uri);
        if (StringUtils.hasText(username)) {
            checkState(StringUtils.hasText(password), () -> "password must provide where username specified");
            props.put("username", username);
            props.put("password", CodecUtil.encrypt(password));
        }
        if (StringUtils.hasText(bearerToken)) {
            props.put("bearerToken", bearerToken);
        }
        if (StringUtils.hasText(kerberosTicket)) {
            props.put("kerberosTicket", kerberosTicket);
        }
        dto.setProps(props);
        return dto;
    }
}
