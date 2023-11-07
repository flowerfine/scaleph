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
 *
 */

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.util.SystemUtil;
import cn.sliew.scaleph.engine.sql.gateway.services.FlinkFactoryService;
import cn.sliew.scaleph.resource.service.JarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.table.factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

@Service
@Slf4j
public class FlinkFactoryServiceImpl implements FlinkFactoryService {

    private final JarService jarService;

    @Autowired
    public FlinkFactoryServiceImpl(JarService jarService) {
        this.jarService = jarService;
    }

    /**
     * A common method to find factories in the given jar.
     *
     * @param id     Jar file id
     * @param tClass Class of the factory
     * @param <T>    Factory
     * @return A factory list
     */
    private <T extends Factory> List<T> findFactories(Long id, Class<T> tClass) {
        Path randomWorkspace = null;
        try {
            randomWorkspace = SystemUtil.getRandomWorkspace();
            String randomFileName = RandomStringUtils.random(8, 'A', 'z', true, false);
            Path randomFilePath = Paths.get(randomFileName, randomFileName + ".jar");
            OutputStream outputStream = Files.newOutputStream(randomFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            String fileName = jarService.download(id, outputStream);
            log.info("Downloaded {} to {}", fileName, randomFileName);
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{randomFilePath.toUri().toURL()}, contextClassLoader)) {
                List<T> list = new ArrayList<>();
                ServiceLoader<T> tServiceLoader = ServiceLoader.load(tClass, urlClassLoader);
                for (T t : tServiceLoader) {
                    list.add(t);
                }
                return list;
            }
        } catch (Exception e) {
            log.error("Error in finding factories!", e);
            return Collections.emptyList();
        } finally {
            if (randomWorkspace != null) {
                try {
                    FileUtil.deleteDir(randomWorkspace);
                    log.info("Deleted tmp dir {}", randomWorkspace);
                } catch (IOException e) {
                    log.error("Error delete tmp dir {}", randomWorkspace);
                }
            }
        }
    }

    @Override
    public List<CatalogFactory> findCatalogs(Long id) {
        return findFactories(id, CatalogFactory.class);
    }

    @Override
    public List<FormatFactory> findFormats(Long id) {
        return findFactories(id, FormatFactory.class);
    }

    @Override
    public List<DynamicTableSourceFactory> findSources(Long id) {
        return findFactories(id, DynamicTableSourceFactory.class);
    }

    @Override
    public List<DynamicTableSinkFactory> findSinks(Long id) {
        return findFactories(id, DynamicTableSinkFactory.class);
    }
}
