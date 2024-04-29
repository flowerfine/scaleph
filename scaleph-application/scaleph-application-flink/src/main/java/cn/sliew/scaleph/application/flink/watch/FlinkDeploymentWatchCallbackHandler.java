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

package cn.sliew.scaleph.application.flink.watch;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceLabels;
import cn.sliew.scaleph.application.flink.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class FlinkDeploymentWatchCallbackHandler implements WatchCallbackHandler<GenericKubernetesResource> {

    @Autowired
    private WsFlinkKubernetesJobInstanceService wsFlinkKubernetesJobInstanceService;

    @Override
    public void onAdded(List<GenericKubernetesResource> resources) {
        resources.forEach(this::handleStatusUpdate);
    }

    @Override
    public void onModified(List<GenericKubernetesResource> resources) {
        resources.forEach(this::handleStatusUpdate);
    }

    @Override
    public void onDeleted(List<GenericKubernetesResource> resources) {
        resources.forEach(this::handleDelete);
    }

    @Override
    public void onError(List<GenericKubernetesResource> resources) {
        resources.forEach(this::handleStatusUpdate);
    }

    @Override
    public void handleError(Throwable throwable) {
        log.error("handle error, {}", throwable);
    }

    private void handleStatusUpdate(GenericKubernetesResource resource) {
        Optional<Long> optional = getJobInstanceIdNumber(resource);
        if (optional.isPresent()) {
            String statusJson = JacksonUtil.toJsonString(resource.get("status"));
            FlinkDeploymentStatus status = JacksonUtil.parseJsonString(statusJson, FlinkDeploymentStatus.class);
            wsFlinkKubernetesJobInstanceService.updateStatus(optional.get(), status);
        }
    }

    private void handleDelete(GenericKubernetesResource resource) {
        Optional<Long> optional = getJobInstanceIdNumber(resource);
        if (optional.isPresent()) {
            wsFlinkKubernetesJobInstanceService.clearStatus(optional.get());
        }
    }

    private Optional<Long> getJobInstanceIdNumber(GenericKubernetesResource resource) {
        Map<String, String> labels = resource.getMetadata().getLabels();
        String jobInstanceIdNumber = labels.get(ResourceLabels.SCALEPH_LABEL_JOB_INSTANCE_ID_NUMBER);
        if (StringUtils.hasText(jobInstanceIdNumber) && NumberUtils.isDigits(jobInstanceIdNumber)) {
            return Optional.of(NumberUtils.createLong(jobInstanceIdNumber));
        }
        return Optional.empty();
    }
}
