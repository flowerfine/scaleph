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

package cn.sliew.scaleph.plugin.seatunnel.flink.resource;

import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import lombok.Getter;

@Getter
public class ResourceProperty<T> {

    private final ResourceType type;
    private final PropertyDescriptor<T> property;

    private ResourceProperty(Builder<T> builder) {
        this.type = builder.type;
        this.property = builder.property;
    }

    public static final class Builder<T> {

        private ResourceType type;
        private PropertyDescriptor<T> property;

        public Builder type(final ResourceType type) {
            if (type != null) {
                this.type = type;
            }
            return this;
        }

        public Builder property(PropertyDescriptor<T> property) {
            if (property != null) {
                this.property = property;
            }
            return this;
        }

        public ResourceProperty validateAndBuild() {
            if (property == null) {
                throw new IllegalStateException("Must specify required property");
            }
            if (type == null) {
                throw new IllegalStateException("Must specify resource type for " + property.getName());
            }

            return new ResourceProperty(this);
        }
    }
}
