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

package cn.sliew.scaleph.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

/**
 * https://github.com/apache/incubator-seatunnel/blob/dev/seatunnel-common/src/main/java/org/apache/seatunnel/common/config/Common.java
 */
public enum SeaTunnelReleaseUtil {
    ;

    private static final int APP_LIB_DIR_DEPTH = 2;

    private static final int PLUGIN_LIB_DIR_DEPTH = 3;

    public static final String STARTER_REPO_URL = "https://repo1.maven.org/maven2/org/apache/seatunnel";

    public static final String STARTER_NAME = "seatunnel-flink-15-starter";
    public static final String STARTER_JAR_NAME = STARTER_NAME + ".jar";
    public static final String SEATUNNEL_MAIN_CLASS = "org.apache.seatunnel.core.starter.flink.SeaTunnelFlink";

    public static final String SEATUNNEL_PLUGIN_MAPPING = "plugin-mapping.properties";

    public static Path getLibDir(Path rootDir) {
        return rootDir.resolve("lib");
    }

    public static List<URL> getLibJars(Path rootDir) {
        Path libRootDir = getLibDir(rootDir);
        if (!Files.exists(libRootDir) || !Files.isDirectory(libRootDir)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.walk(libRootDir, APP_LIB_DIR_DEPTH, FOLLOW_LINKS)) {
            return stream.filter(it -> !it.toFile().isDirectory())
                    .filter(it -> it.getFileName().toString().endsWith(".jar"))
                    .map(it -> {
                        try {
                            return it.toFile().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getStarterDir(Path rootDir) {
        return rootDir.resolve("starter");
    }

    public static Path getStarterJarPath(Path rootDir) {
        return getStarterDir(rootDir).resolve(STARTER_JAR_NAME);
    }

    /**
     * Connector Root Dir
     */
    public static Path connectorsRootDir(Path rootDir) {
        return Paths.get(rootDir.toString(), "connectors");
    }

    public static Path getPluginMapping(Path rootDir) {
        return connectorsRootDir(rootDir).resolve(SEATUNNEL_PLUGIN_MAPPING);
    }

    /**
     * Engine Connector Root Dir
     */
    public static Path engineConnectorsRootDir(Path rootDir, String engine) {
        return connectorsRootDir(rootDir).resolve(engine.toLowerCase());
    }

    public static Path seatunnelConnectorsRootDir(Path rootDir) {
        return engineConnectorsRootDir(rootDir, "seatunnel");
    }

    public static Path seatunnelConnector(Path rootDir, String connector) {
        return seatunnelConnectorsRootDir(rootDir).resolve(connector);
    }

    public static String seatunnelStarterUrl(String repoUrl, String version) {
        if (StringUtils.endsWithIgnoreCase(repoUrl, "/")) {
            repoUrl = StringUtils.removeEndIgnoreCase(repoUrl, "/");
        }
        Map<String, String> variables = Map.of(
                "repoUrl", repoUrl,
                "version", version,
                "connector", STARTER_NAME,
                "jar", convertToJar(version, STARTER_NAME));
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        String template = "${repoUrl}/${connector}/${version}/${jar}";
        return substitutor.replace(template);
    }

    public static String seatunnelConnectorUrl(String repoUrl, String version, String connector) {
        if (StringUtils.endsWithIgnoreCase(repoUrl, "/")) {
            repoUrl = StringUtils.removeEndIgnoreCase(repoUrl, "/");
        }
        Map<String, String> variables = Map.of(
                "repoUrl", repoUrl,
                "version", version,
                "connector", connector,
                "jar", convertToJar(version, connector));
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        String template = "${repoUrl}/${connector}/${version}/${jar}";
        return substitutor.replace(template);
    }

    public static String convertToJar(String version, String connector) {
        Map<String, String> variables = Map.of("version", version, "connector", connector);
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        String template = "${connector}-${version}.jar";
        return substitutor.replace(template);
    }

    public static boolean isV2Connectors(String plugin) {
        return StringUtils.startsWithIgnoreCase(plugin, "seatunnel");
    }

}
