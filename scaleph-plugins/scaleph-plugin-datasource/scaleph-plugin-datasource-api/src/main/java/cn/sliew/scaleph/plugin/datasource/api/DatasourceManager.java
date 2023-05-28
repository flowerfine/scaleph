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

package cn.sliew.scaleph.plugin.datasource.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

public class DatasourceManager {

    private final PluginSPILoader<DatasourcePlugin> pluginPluginSPILoader;

    public DatasourceManager(Path pluginPath) {
        List<ClassLoader> classLoaders = new ArrayList<>();
        if (pluginPath != null && Files.exists(pluginPath) && Files.isDirectory(pluginPath)) {
            try (Stream<Path> pluginDirectories = Files.list(pluginPath)) {
                classLoaders = pluginDirectories.filter(Files::isDirectory)
                        .map(this::buildClassLoaderByPath)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        this.pluginPluginSPILoader = new PluginSPILoader<>(DatasourcePlugin.class, classLoaders.toArray(new ClassLoader[0]));
    }

    public Set<PluginInfo> getAvailableDataSources() {
        return pluginPluginSPILoader.availableServices();
    }

    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        final Optional<DatasourcePlugin> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        final DatasourcePlugin dataSourcePlugin = optional.orElseThrow(() -> new IllegalStateException("unknown plugin info for " + pluginInfo));
        return dataSourcePlugin.getSupportedProperties();
    }

    public <T> DatasourcePlugin<T> newDatasourcePlugin(String name, Map<String, Object> props,
                                                       Map<String, Object> additionalProps) {
        Properties properties = new Properties();
        properties.putAll(props);
        final DatasourcePlugin datasourcePlugin = pluginPluginSPILoader.newInstance(name, properties);
        Properties additionalProperties = new Properties();
        additionalProperties.putAll(additionalProps);
        datasourcePlugin.setAdditionalProperties(additionalProperties);
        datasourcePlugin.start();
        return datasourcePlugin;
    }

    private ClassLoader buildClassLoaderByPath(Path pluginDirectory) {
        ClassLoader classLoader;
        Function<Path, Boolean> filter = p -> Files.isRegularFile(p) && p.toString().endsWith(".jar");
        try {
            URL[] urls = FileUtil.listFilesRecursively(pluginDirectory, filter)
                    .stream()
                    .map(path -> {
                        try {
                            return path.toUri().toURL();
                        } catch (MalformedURLException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toArray(URL[]::new);
            classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classLoader;
    }

}
