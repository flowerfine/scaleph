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

import cn.sliew.scaleph.dao.entity.master.ds.DsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MySQLDataSource extends AbstractDataSource {

    @ApiModelProperty("driver class name")
    private String driverClassName;

    @ApiModelProperty("jdbc url")
    private String url;

    @ApiModelProperty("user")
    private String user;

    @ApiModelProperty("user")
    private String password;

    @Override
    protected DsInfo toRecord() {
        return null;
    }
}
