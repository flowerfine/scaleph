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

package cn.sliew.scaleph.application.doris.service.resource.template;

import cn.sliew.scaleph.application.doris.operator.spec.DorisClusterSpec;
import cn.sliew.scaleph.application.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.resource.Resource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Group(Constant.SCALEPH_GROUP)
@Version(Constant.SCALEPH_VERSION)
@EqualsAndHashCode
@JsonPropertyOrder({"apiVersion", "kind", "metadata", "spec", "status"})
public class DorisTemplate extends CustomResource<DorisClusterSpec, DorisClusterStatus> implements Resource {
}
