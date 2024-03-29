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

package cn.sliew.scaleph.security.web;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 使用jwt或者uuid等规则生成用户的token
 *
 * @author gleiyu
 */
@Component
public class TokenProvider {

    /**
     * 使用uuid作为token
     */
    public String createToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
