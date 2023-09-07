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

package cn.sliew.scaleph.engine.sql.gateway.services;

import org.apache.flink.table.gateway.service.session.Session;

import java.util.Map;

/**
 * 多级的 session 管理。
 * 默认配置。scaleph 系统级别
 * 全局配置。用户全局配置级别
 * 用户级别。和用户 id 关联 || 项目级别。和项目 id 关联
 */
public interface SessionService {

    // -------------------------------------------------------------------------------------------
    // Global Session
    // -------------------------------------------------------------------------------------------

    Map<String, String> getGlobalSessionConfig() throws Exception;

    void configureGlobalSession(Map<String, String> config) throws Exception;

    void configureGlobalSession(String statement) throws Exception;

    // -------------------------------------------------------------------------------------------
    // User Session || Project Session
    // -------------------------------------------------------------------------------------------

    Session openSession() throws Exception;

    void closeSession() throws Exception;

    Map<String, String> getSessionConfig() throws Exception;

    void configureSession(Map<String, String> config) throws Exception;

    void configureSession(String statement) throws Exception;
}
