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

import java.util.Optional;

public interface JobClient {

    MultipleJobsDetails overview();

    JobIdsWithStatusOverview statusOverview();

    JobDetailsInfo detail(String jobId);

    JobConfigInfo config(String jobId);

    JobExceptionsInfoWithHistory exceptioins(String jobId, Optional<String> maxExceptions);

    CheckpointConfigInfo checkpointConfig(String jobId);

    CheckpointingStatistics checkpoints(String jobId);

    CheckpointStatistics checkpointDetail(String jobId, Long checkpointId);

    JobPlanInfo plan(String jobId);
}
