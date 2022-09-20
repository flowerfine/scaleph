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

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public class PluginInfo {

    public static final String PLUGIN_PROPERTIES = "plugin.properties";

    private final String name;
    private final String description;
    private final String classname;

    public PluginInfo(String name, String description, String classname) {
        this.name = name;
        this.description = description;
        this.classname = classname;
    }

    /**
     * Reads the plugin descriptor file.
     *
     * @param properties the path to the root directory for the plugin
     * @return the plugin info
     * @throws IOException if an I/O exception occurred reading the plugin descriptor
     */
    public static PluginInfo readFromProperties(Properties properties) throws IOException {

        final Map<String, String> propsMap = properties.stringPropertyNames().stream()
                .collect(Collectors.toMap(Function.identity(), properties::getProperty));

        final String name = propsMap.remove("name");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("property [name] is missing");
        }
        final String description = propsMap.remove("description");
        if (description == null) {
            throw new IllegalArgumentException(
                    "property [description] is missing for plugin [" + name + "]");
        }
        final String version = propsMap.remove("version");
        if (version == null) {
            throw new IllegalArgumentException(
                    "property [version] is missing for plugin [" + name + "]");
        }

        final String classname = propsMap.remove("classname");

        if (propsMap.isEmpty() == false) {
            throw new IllegalArgumentException(
                    "Unknown properties for plugin [" + name + "] in plugin descriptor: " +
                            propsMap.keySet());
        }

        return new PluginInfo(name, description, classname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PluginInfo that = (PluginInfo) o;
        return name.equals(that.name) && classname.equals(that.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name + classname);
    }
}
