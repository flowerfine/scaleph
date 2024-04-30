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

package cn.sliew.scaleph.application.doris.operator.spec;

import lombok.Data;

/**
 * DorisClusterSpec defines the desired state of DorisCluster
 */
@Data
public class DorisClusterSpec {

    /**
     * defines the fe cluster state that will be created by operator.
     */
    private FeSpec feSpec;

    /**
     * defines the be cluster state pod that will be created by operator.
     */
    private BeSpec beSpec;

    /**
     * defines the cn cluster state that will be created by operator.
     */
    private CnSpec cnSpec;

    /**
     * efines the broker state that will be created by operator.
     */
    private BrokerSpec brokerSpec;

    /**
     * administrator for register or drop component from fe cluster. adminUser for all component register and operator drop component.
     */
    private AdminUser adminUser;
}
