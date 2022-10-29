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

package cn.sliew.scaleph.plugin.framework.exception;

import cn.sliew.scaleph.common.exception.ScalephException;

public class PluginException extends ScalephException {

    private final String clazz;

    public PluginException(String clazz) {
        super();
        this.clazz = clazz;
    }

    public PluginException(String clazz, String message) {
        super(message);
        this.clazz = clazz;
    }

    public PluginException(String clazz, Throwable cause) {
        super(cause);
        this.clazz = clazz;
    }

    public PluginException(String clazz, String message, Throwable cause) {
        super(message, cause);
        this.clazz = clazz;
    }

    protected PluginException(String clazz, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }

    public PluginException(String clazz, String code, String message) {
        super(code, message);
        this.clazz = clazz;
    }
}
