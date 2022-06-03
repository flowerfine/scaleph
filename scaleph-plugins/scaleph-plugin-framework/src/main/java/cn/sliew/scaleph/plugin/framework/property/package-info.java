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

/**
 * inspired by
 * <a href="https://github.com/apache/nifi/blob/main/nifi-api/src/main/java/org/apache/nifi/components/PropertyDescriptor.java">nifi</a>,
 * <a href="https://github.com/apache/flink/blob/master/flink-core/src/main/java/org/apache/flink/configuration/ConfigOption.java">flink</a>,
 * <a href="https://github.com/elastic/elasticsearch/blob/master/server/src/main/java/org/elasticsearch/common/settings/Setting.java">elasticsearch</a>
 */
package cn.sliew.scaleph.plugin.framework.property;