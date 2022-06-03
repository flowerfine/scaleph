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

package cn.sliew.scaleph.plugin.framework.property;

import lombok.Getter;

@Getter
public class ValidationResult {

    private final String subject;
    private final String input;
    private final String explanation;
    private final boolean valid;

    protected ValidationResult(final Builder builder) {
        this.subject = builder.subject;
        this.input = builder.input;
        this.explanation = builder.explanation;
        this.valid = builder.valid;
    }

    public static final class Builder {

        private boolean valid = false;
        private String input = null;
        private String explanation = "";
        private String subject = "";

        public Builder valid(final boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder input(final String input) {
            if (null != input) {
                this.input = input;
            }
            return this;
        }

        public Builder explanation(final String explanation) {
            if (null != explanation) {
                this.explanation = explanation;
            }
            return this;
        }

        public Builder subject(final String subject) {
            if (null != subject) {
                this.subject = subject;
            }
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(this);
        }
    }
}
