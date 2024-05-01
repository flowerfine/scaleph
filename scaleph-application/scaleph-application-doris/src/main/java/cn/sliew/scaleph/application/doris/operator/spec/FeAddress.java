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
 * FeAddress specify the fe address, please set it when you deploy fe outside k8s or deploy components use crd except fe, if not set .
 */
@Data
public class FeAddress {

    /**
     * the service name that proxy fe on k8s. the service must in same namespace with fe.
     */
    private String serviceName;

    /**
     * the fe addresses if not deploy by crd, user can use k8s deploy fe observer.
     */
    private Endpoints endpoints;
}
