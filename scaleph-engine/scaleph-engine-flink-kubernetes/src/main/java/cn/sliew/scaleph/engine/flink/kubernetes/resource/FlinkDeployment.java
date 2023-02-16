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

package cn.sliew.scaleph.engine.flink.kubernetes.resource;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.entity.Constant;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class FlinkDeployment implements Resource {

    private FlinkDeploymentMetadata metadata;
    private FlinkDeploymentSpec spec;

    @Override
    public String getKind() {
        return Constant.FLINK_DEPLOYMENT;
    }

    @Override
    public String getApiVersion() {
        return Constant.API_VERSION;
    }

    @Override
    public ResourceMetadata getMetadata() {
        return metadata;
    }

    @Data
    @EqualsAndHashCode
    public static class FlinkDeploymentMetadata implements ResourceMetadata {

        private Long id;
        private String name;
        private String namespace;
        private Date createTime;
        private Date updateTime;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getNamespace() {
            return namespace;
        }

        @Override
        public Date getCreateTime() {
            return createTime;
        }

        @Override
        public Date getUpdateTime() {
            return updateTime;
        }
    }
}
