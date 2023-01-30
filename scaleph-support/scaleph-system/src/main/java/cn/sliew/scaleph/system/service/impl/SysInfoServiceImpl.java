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

package cn.sliew.scaleph.system.service.impl;

import cn.sliew.scaleph.system.service.SysInfoService;
import cn.sliew.scaleph.system.service.dto.JavaInfoDTO;
import cn.sliew.scaleph.system.service.dto.RevisionInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.info.JavaInfo;
import org.springframework.boot.info.OsInfo;
import org.springframework.stereotype.Service;

@Service
public class SysInfoServiceImpl implements SysInfoService {

//    @Autowired
    private BuildProperties buildProperties;
//    @Autowired
    private GitProperties gitProperties;
//    @Autowired
    private JavaInfo javaInfo;
//    @Autowired
    private OsInfo osInfo;

    @Override
    public RevisionInfoDTO getRevisionInfo() {
        RevisionInfoDTO dto = new RevisionInfoDTO();
        dto.setCommitLongId(gitProperties.getCommitId());
        dto.setCommitShortId(gitProperties.getShortCommitId());
        dto.setBuildVersion(buildProperties.getVersion());
        dto.setBuildTime(buildProperties.getTime().toString());
        return dto;
    }

    @Override
    public JavaInfoDTO getJavaInfo() {
        JavaInfoDTO dto = new JavaInfoDTO();
        String name = javaInfo.getVendor().getName();
        return dto;
    }
}
