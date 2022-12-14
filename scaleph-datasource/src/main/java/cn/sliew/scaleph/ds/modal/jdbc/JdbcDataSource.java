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

package cn.sliew.scaleph.ds.modal.jdbc;

import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.jackson.sensitive.Sensitive;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.dto.DsTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class JdbcDataSource extends AbstractDataSource {

    @NotBlank
    @ApiModelProperty("driver class name")
    private String driverClassName;

    @NotBlank
    @ApiModelProperty("jdbc url")
    private String url;

    @ApiModelProperty("user")
    private String user;

    /**
     * {@link Sensitive} not affect Map<String, Object> props
     *
     * @see Sensitive
     */
    @ApiModelProperty("password")
    private String password;

    @Override
    public DsInfoDTO toDsInfo() {
        DsInfoDTO dto = BeanUtil.copy(this, new DsInfoDTO());
        DsTypeDTO dsType = new DsTypeDTO();
        dsType.setId(getDsTypeId());
        dsType.setType(getType());
        dto.setDsType(dsType);
        String encryptedPass = CodecUtil.encrypt(password);
        Map<String, Object> props = Map.of("driverClassName", driverClassName, "url", url, "user", user, "password", encryptedPass);
        dto.setProps(props);
        return dto;
    }
}
