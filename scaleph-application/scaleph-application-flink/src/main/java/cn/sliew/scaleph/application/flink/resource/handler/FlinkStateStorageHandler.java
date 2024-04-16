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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.config.storage.FileSystemType;
import cn.sliew.scaleph.config.storage.S3FileSystemProperties;
import cn.sliew.scaleph.application.flink.operator.spec.AbstractFlinkSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FlinkStateStorageHandler {

    @Autowired(required = false)
    private S3FileSystemProperties s3FileSystemProperties;

    public void handle(String jobInstanceId, AbstractFlinkSpec spec) {
        Map<String, String> configuration = Optional.ofNullable(spec.getFlinkConfiguration()).orElse(new HashMap<>());
        addStateStorageConfigOption(jobInstanceId, configuration);
        spec.setFlinkConfiguration(configuration);
    }

    private void addStateStorageConfigOption(String jobInstanceId, Map<String, String> configuration) {
        String schemaAndPath = getSchemaAndPath();
        configuration.put("state.checkpoints.dir", getCheckpointPath(schemaAndPath, jobInstanceId));
        configuration.put("state.savepoints.dir", getSavepointPath(schemaAndPath, jobInstanceId));
        configuration.put("high-availability.storageDir", getHaPath(schemaAndPath, jobInstanceId));
        configuration.put("high-availability", "org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory");
    }

    private String getSchemaAndPath() {
        FileSystemType fileSystemType = FileSystemType.of(s3FileSystemProperties.getType());
        return String.format("%s%s", fileSystemType.getSchema(), s3FileSystemProperties.getBucket());
    }

    private static String getCheckpointPath(String schema, String jobInstanceId) {
        return String.format("%s/flink/jobs/%s/checkpoints/", schema, jobInstanceId);
    }

    private static String getSavepointPath(String schema, String jobInstanceId) {
        return String.format("%s/flink/jobs/%s/savepoints/", schema, jobInstanceId);
    }

    private static String getHaPath(String schema, String jobInstanceId) {
        return String.format("%s/flink/jobs/%s/ha/", schema, jobInstanceId);
    }

}
