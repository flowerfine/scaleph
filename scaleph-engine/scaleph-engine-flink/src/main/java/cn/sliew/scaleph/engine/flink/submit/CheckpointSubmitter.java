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

package cn.sliew.scaleph.engine.flink.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.StateBackendOptions;

/**
 * checkpoint 支持的实现有 filesystem 和 rocksdb
 */
public class CheckpointSubmitter implements Submitter {

    /**
     * todo does it need baseurl?
     */
    @Override
    public void configure(Configuration configuration) {
        configuration.set(StateBackendOptions.STATE_BACKEND, "hashmap");
        configuration.set(CheckpointingOptions.CHECKPOINT_STORAGE, "filesystem");
        configuration.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY, "");
        configuration.set(CheckpointingOptions.SAVEPOINT_DIRECTORY, "");
    }

    @Override
    public void submit() {
        PackageJarJob job = new PackageJarJob();
    }
}
