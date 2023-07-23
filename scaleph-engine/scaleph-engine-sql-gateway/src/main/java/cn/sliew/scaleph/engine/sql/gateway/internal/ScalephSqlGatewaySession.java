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

package cn.sliew.scaleph.engine.sql.gateway.internal;

import org.apache.flink.table.gateway.service.context.SessionContext;

import java.time.Duration;
import java.util.UUID;

public class ScalephSqlGatewaySession {
    private long lastAccessTime;
    private SessionContext sessionContext;

    public static ScalephSqlGatewaySession create(SessionContext sessionContext) {
        return new ScalephSqlGatewaySession(sessionContext);
    }

    ScalephSqlGatewaySession(SessionContext sessionContext) {
        this.lastAccessTime = System.currentTimeMillis();
        this.sessionContext = sessionContext;
    }

    public void touch() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public ScalephSqlGatewaySession setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public ScalephSqlGatewaySession setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
        return this;
    }

    public boolean isExpired(Duration duration) {
        return System.currentTimeMillis() - lastAccessTime > duration.toMillis();
    }

    public void close() {
        this.sessionContext.close();
    }

    public UUID getIdentifier() {
        return sessionContext.getSessionId().getIdentifier();
    }
}
