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

package cn.sliew.scaleph.plugin.framework.core;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;

/**
 * A "bundle" is a group of jars that will be loaded in their own classloader
 */
@EqualsAndHashCode
public class PluginBundle {

    public final PluginInfo plugin;
    public final Set<URL> urls;
    public final Set<URL> spiUrls;
    public final Set<URL> allUrls;

    public PluginBundle(PluginInfo plugin, Path dir) throws IOException {
        this.plugin = Objects.requireNonNull(plugin);

        Path spiDir = dir.resolve("spi");
        // plugin has defined an explicit api for extension
        this.spiUrls = Files.exists(spiDir) ? gatherUrls(spiDir) : null;
        this.urls = gatherUrls(dir);
        Set<URL> allUrls = new LinkedHashSet<>(urls);
        if (spiUrls != null) {
            allUrls.addAll(spiUrls);
        }
        this.allUrls = allUrls;
    }

    static Set<URL> gatherUrls(Path dir) throws IOException {
        Set<URL> urls = new LinkedHashSet<>();
        // gather urls for jar files
        try (DirectoryStream<Path> jarStream = Files.newDirectoryStream(dir, "*.jar")) {
            for (Path jar : jarStream) {
                // normalize with toRealPath to get symlinks out of our hair
                URL url = jar.toRealPath().toUri().toURL();
                if (urls.add(url) == false) {
                    throw new IllegalStateException("duplicate codebase: " + url);
                }
            }
        }
        return urls;
    }

    Set<URL> getExtensionUrls() {
        if (spiUrls != null) {
            return spiUrls;
        }
        return urls;
    }
}
