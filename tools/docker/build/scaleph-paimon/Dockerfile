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
ARG FLINK_CDC_VERSION=3.0.1
ARG PAIMON_VERSION=0.7.0
FROM eclipse-temurin:11-focal as build
ENV BUILD_HOME=/opt/build
RUN mkdir -p BUILD_HOME

## todo 切换成 pom.xml
## jdbc driver
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=mysql -DartifactId=mysql-connector-java -Dversion=8.0.28 -Ddest=${BUILD_HOME}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.3.3 -Ddest=${BUILD_HOME}

## flink cdc connectors
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=com.ververica -DartifactId=flink-connector-mysql-cdc -Dversion=${FLINK_CDC_VERSION} -Ddest=${BUILD_HOME}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=com.ververica -DartifactId=flink-connector-postgres-cdc -Dversion=${FLINK_CDC_VERSION} -Ddest=${BUILD_HOME}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=com.ververica -DartifactId=flink-sql-connector-mongodb-cdc -Dversion=${FLINK_CDC_VERSION} -Ddest=${BUILD_HOME}

## flink connectors
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=org.apache.flink -DartifactId=flink-sql-connector-kafka -Dversion=3.1.0-1.18 -Ddest=${BUILD_HOME}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=org.apache.flink -DartifactId=flink-connector-pulsar -Dversion=4.1.0-1.18 -Ddest=${BUILD_HOME}

## paimon cdc action
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=org.apache.paimon -DartifactId=paimon-flink-action -Dversion=${PAIMON_VERSION} -Ddest=${BUILD_HOME}

ARG FLINK_VERSION=1.18
ARG FLINK_CDC_VERSION=3.0.1
ARG PAIMON_VERSION=0.7.0
FROM flink:${FLINK_VERSION} as release
ENV BUILD_HOME=/opt/build

COPY --from=build /opt/build/*.jar /opt/paimon