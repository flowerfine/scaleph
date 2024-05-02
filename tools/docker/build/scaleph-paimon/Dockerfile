# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ARG FLINK_VERSION=1.18
FROM maven:3.8-eclipse-temurin-11 as build

ENV BUILD_HOME=/opt/build
RUN mkdir -p ${BUILD_HOME}

ENV CONF_HOME=/opt/conf
RUN mkdir -p ${CONF_HOME}

# 下载 pom.xml 中 jar 至指定目录
COPY tools/docker/build/scaleph-paimon/pom.xml ${CONF_HOME}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:copy-dependencies -f ${CONF_HOME}/pom.xml -DexcludeTransitive=true -DoutputDirectory=${BUILD_HOME}


FROM flink:${FLINK_VERSION} as release
ENV BUILD_HOME=/opt/build

COPY --from=build ${BUILD_HOME}/*.jar /opt/paimon