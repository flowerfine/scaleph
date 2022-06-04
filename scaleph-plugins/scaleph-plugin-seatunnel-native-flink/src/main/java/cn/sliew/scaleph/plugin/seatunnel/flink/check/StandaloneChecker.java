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

package cn.sliew.scaleph.plugin.seatunnel.flink.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;

public class StandaloneChecker implements ConfigurationChecker {

    public static List<ConfigOption> requiredConfigOptions;

    static {
        requiredConfigOptions = new ArrayList<>();
        requiredConfigOptions.add(JobManagerOptions.ADDRESS);
    }

    @Override
    public List<CheckResult> check(Configuration configuration) {
        List<CheckResult> results = new ArrayList<>();
        for (ConfigOption configOption : requiredConfigOptions) {
            final CheckResult.Builder builder = new CheckResult.Builder();
            builder.configOption(configOption);
            if (configuration.contains(configOption) == false) {
                builder.valid(false).explanation("require " + configOption.key());
            } else {
                builder.valid(true);
            }
            results.add(builder.build());
        }
        return results;
    }
}
