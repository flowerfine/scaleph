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

package cn.sliew.scaleph.system.util;

import cn.hutool.core.lang.UUID;
import cn.sliew.scaleph.common.nio.FileUtil;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class SystemUtil implements InitializingBean, DisposableBean {

    private static String workspace;

    @Value("${app.workspace}")
    public void setWorkspace(String workspace) {
        SystemUtil.workspace = workspace;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileUtil.deleteDir(Path.of(workspace));
    }

    @Override
    public void destroy() throws Exception {
        FileUtil.deleteDir(Path.of(workspace));
    }

    public static Path getWorkspace() throws IOException {
        return FileUtil.createDir(Path.of(workspace));
    }

    public static Path getRandomWorkspace() throws IOException {
        return FileUtil.createDir(getWorkspace().resolve(UUID.randomUUID().toString(true)));
    }
}
