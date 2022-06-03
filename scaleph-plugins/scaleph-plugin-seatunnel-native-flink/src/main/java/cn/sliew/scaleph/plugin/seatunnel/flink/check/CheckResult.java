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

import org.apache.flink.configuration.ConfigOption;

public class CheckResult {

    private ConfigOption configOption;
    private boolean valid;
    private String explanation;

    protected CheckResult(final Builder builder) {
        this.configOption = builder.configOption;
        this.valid = builder.valid;
        this.explanation = builder.explanation;
    }

    public static final class Builder {
        private ConfigOption configOption;
        private boolean valid;
        private String explanation;

        public Builder configOption(final ConfigOption configOption) {
            if (configOption != null) {
                this.configOption = configOption;
            }
            return this;
        }

        public Builder valid(final boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder explanation(final String explanation) {
            if (explanation != null) {
                this.explanation = explanation;
            }
            return this;
        }

        public CheckResult build() {
            return new CheckResult(this);
        }
    }
}
