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

package cn.sliew.scaleph.engine.flink.client.v1.api;

import cn.sliew.scaleph.engine.flink.client.v1.model.job.*;
import cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint.CheckpointConfigInfo;
import cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint.CheckpointStatistics;
import cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint.CheckpointingStatistics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Optional;

@FeignClient(value = "FlinkJobClient", url = "EMPTY")
public interface JobClient {

    @GetMapping(value = "v1/jobs/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    MultipleJobsDetails overview(URI uri);

    @GetMapping(value = "v1/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    JobIdsWithStatusOverview statusOverview(URI uri);

    @GetMapping(value = "v1/jobs/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    JobDetailsInfo detail(URI uri, @PathVariable("jobId") String jobId);

    @GetMapping(value = "v1/jobs/{jobId}/config", produces = MediaType.APPLICATION_JSON_VALUE)
    JobConfigInfo config(URI uri, @PathVariable("jobId") String jobId);

    @GetMapping(value = "v1/jobs/{jobId}/exceptions", produces = MediaType.APPLICATION_JSON_VALUE)
    JobExceptionsInfoWithHistory exceptioins(URI uri, @PathVariable("jobId") String jobId, @RequestParam("maxExceptions") Optional<String> maxExceptions);

    @GetMapping(value = "v1/jobs/{jobId}/checkpoints/config", produces = MediaType.APPLICATION_JSON_VALUE)
    CheckpointConfigInfo checkpointConfig(URI uri, @PathVariable("jobId") String jobId);

    @GetMapping(value = "v1/jobs/{jobId}/checkpoints", produces = MediaType.APPLICATION_JSON_VALUE)
    CheckpointingStatistics checkpoints(URI uri, @PathVariable("jobId") String jobId);

    @GetMapping(value = "v1/jobs/{jobId}/checkpoints/details/{checkpointId}", produces = MediaType.APPLICATION_JSON_VALUE)
    CheckpointStatistics checkpointDetail(URI uri, @PathVariable("jobId") String jobId, @PathVariable("checkpointId") Long checkpointId);

    @GetMapping(value = "v1/jobs/{jobId}/plan", produces = MediaType.APPLICATION_JSON_VALUE)
    JobPlanInfo plan(URI uri, @PathVariable("jobId") String jobId);
}
