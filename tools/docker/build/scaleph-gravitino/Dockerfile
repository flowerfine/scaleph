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

ARG GRAVITINO_VERSION=0.5.0
FROM maven:3.8-eclipse-temurin-11 as build

ENV INSTALL_HOME=/opt/install
RUN mkdir -p ${INSTALL_HOME}

## install mysql jdbc driver
ENV JDBC_MYSQL_LIBS=${INSTALL_HOME}/catalogs/jdbc-mysql/libs
RUN mkdir -p ${JDBC_MYSQL_LIBS}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=mysql -DartifactId=mysql-connector-java -Dversion=8.0.28 -Dtransitive=false -Ddest=${JDBC_MYSQL_LIBS}

## install postgresql jdbc driver
ENV JDBC_POSTGRESQL_LIBS=${INSTALL_HOME}/catalogs/jdbc-postgresql/libs
RUN mkdir -p ${JDBC_POSTGRESQL_LIBS}
RUN --mount=type=cache,target=/root/.m2 mvn dependency:get -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=42.3.3 -Dtransitive=false -Ddest=${JDBC_POSTGRESQL_LIBS}

FROM datastrato/gravitino:${GRAVITINO_VERSION} as release
ENV INSTALL_HOME=/opt/install
ENV JDBC_MYSQL_LIBS=${INSTALL_HOME}/catalogs/jdbc-mysql/libs
ENV JDBC_POSTGRESQL_LIBS=${INSTALL_HOME}/catalogs/jdbc-postgresql/libs

COPY --from=build ${JDBC_MYSQL_LIBS}/*.jar root/gravitino/catalogs/jdbc-mysql/libs
COPY --from=build ${JDBC_POSTGRESQL_LIBS}/*.jar root/gravitino/catalogs/jdbc-postgresql/libs