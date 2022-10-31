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

package cn.sliew.scaleph.storage.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * forked from <a href="https://github.com/apache/flink/blob/master/flink-connectors/flink-hadoop-compatibility/src/main/java/org/apache/flink/api/java/hadoop/mapred/utils/HadoopUtils.java">HadoopUtils</>
 */
@Slf4j
public enum HadoopUtil {
    ;

    /**
     * Returns a new Hadoop Configuration object using the path to the hadoop conf.
     *
     * @return A Hadoop configuration instance
     */
    public static Configuration getHadoopConfiguration(String hadoopConfPath) {

        Configuration retConf = new Configuration();
        retConf.setBoolean(CommonConfigurationKeys.IPC_CLIENT_PING_KEY, false);
        retConf.setInt(CommonConfigurationKeys.IPC_CLIENT_RPC_TIMEOUT_KEY, 1000 * 10);
        retConf.setBoolean(CommonConfigurationKeys.IPC_SERVER_LOG_SLOW_RPC, true);
        retConf.setBoolean(CommonConfigurationKeysPublic.IPC_SERVER_LOG_SLOW_RPC, true);

        // We need to load both core-site.xml and hdfs-site.xml to determine the default fs path and
        // the hdfs configuration
        // Try to load HDFS configuration from Hadoop's own configuration files

        // Approach environment variables
        for (String possibleHadoopConfPath : possibleHadoopConfPaths(hadoopConfPath)) {
            if (new File(possibleHadoopConfPath).exists()) {
                if (new File(possibleHadoopConfPath + "/core-site.xml").exists()) {
                    retConf.addResource(
                            new org.apache.hadoop.fs.Path(
                                    possibleHadoopConfPath + "/core-site.xml"));

                    if (log.isDebugEnabled()) {
                        log.debug(
                                "Adding "
                                        + possibleHadoopConfPath
                                        + "/core-site.xml to hadoop configuration");
                    }
                }
                if (new File(possibleHadoopConfPath + "/hdfs-site.xml").exists()) {
                    retConf.addResource(
                            new org.apache.hadoop.fs.Path(
                                    possibleHadoopConfPath + "/hdfs-site.xml"));

                    if (log.isDebugEnabled()) {
                        log.debug(
                                "Adding "
                                        + possibleHadoopConfPath
                                        + "/hdfs-site.xml to hadoop configuration");
                    }
                }
            }
        }
        return retConf;
    }

    /**
     * Get possible Hadoop conf dir paths, based on environment variables and flink configuration.
     *
     * @return an array of possible paths
     */
    public static String[] possibleHadoopConfPaths(String hadoopConfPath) {
        String[] possiblePaths = new String[4];
        possiblePaths[0] = hadoopConfPath;
        possiblePaths[1] = System.getenv("HADOOP_CONF_DIR");

        if (System.getenv("HADOOP_HOME") != null) {
            possiblePaths[2] = System.getenv("HADOOP_HOME") + "/conf";
            possiblePaths[3] = System.getenv("HADOOP_HOME") + "/etc/hadoop"; // hadoop 2.2
        }
        return Arrays.stream(possiblePaths).filter(Objects::nonNull).toArray(String[]::new);
    }
}
