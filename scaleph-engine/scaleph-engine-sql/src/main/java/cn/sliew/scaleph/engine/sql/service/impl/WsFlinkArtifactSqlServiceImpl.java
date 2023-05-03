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

package cn.sliew.scaleph.engine.sql.service.impl;

import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkArtifactSqlMapper;
import cn.sliew.scaleph.engine.sql.service.WsFlinkArtifactSqlService;
import cn.sliew.scaleph.engine.sql.service.convert.WsFlinkArtifactSqlConvert;
import cn.sliew.scaleph.engine.sql.service.dto.WsFlinkArtifactSqlDTO;
import cn.sliew.scaleph.engine.sql.service.param.WsFlinkArtifactSqlParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class WsFlinkArtifactSqlServiceImpl implements WsFlinkArtifactSqlService {

    @Autowired
    private WsFlinkArtifactSqlMapper wsFlinkArtifactSqlMapper;

    @Override
    public Page<WsFlinkArtifactSqlDTO> list(WsFlinkArtifactSqlParam param) {
        Page<WsFlinkArtifactSql> page = new Page<>(param.getCurrent(), param.getPageSize());
        WsFlinkArtifactSql wsFlinkArtifactSql = BeanUtil.copy(param, new WsFlinkArtifactSql());
        Page<WsFlinkArtifactSql> sqlPage = wsFlinkArtifactSqlMapper.list(page, wsFlinkArtifactSql);
        Page<WsFlinkArtifactSqlDTO> result =
                new Page<>(sqlPage.getCurrent(), sqlPage.getSize(), sqlPage.getTotal());
        result.setRecords(WsFlinkArtifactSqlConvert.INSTANCE.toDto(sqlPage.getRecords()));
        return result;
    }

    @Override
    public WsFlinkArtifactSqlDTO selectOne(Long id) {
        WsFlinkArtifactSql wsFlinkArtifactSql = wsFlinkArtifactSqlMapper.selectOne(id);
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSql);
    }

    @Override
    public int deleteOne(Long id) throws ScalephException {
        return wsFlinkArtifactSqlMapper.deleteById(id);
    }

    @Override
    public void insert(WsFlinkArtifactSqlDTO param, MultipartFile file) throws IOException {

    }

    @Override
    public int update(WsFlinkArtifactSqlDTO params) {
        return 0;
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        return null;
    }

    @Override
    public List<WsFlinkArtifactSqlDTO> listByArtifact(Long artifactId) {
        return null;
    }
}
