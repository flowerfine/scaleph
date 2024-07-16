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

FROM flink:${FLINK_VERSION}

ARG FLINK_CDC_VERSION=3.1.1
ARG TAR_FILE=flink-cdc-${FLINK_CDC_VERSION}-bin.tar.gz

ENV FLINK_CDC_HOME=/opt/flink-cdc
ENV FLINK_CDC_CONF=$FLINK_CDC_HOME/conf
ENV FLINK_CDC_LIB=$FLINK_CDC_HOME/lib
ENV FLINK_CDC_LOG=$FLINK_CDC_HOME/log
RUN mkdir -p $FLINK_CDC_HOME

RUN mkdir -p $FLINK_CDC_HOME/yaml
COPY tools/docker/build/flink-cdc/mysql-to-kafka.yaml $FLINK_CDC_HOME/yaml/mysql-to-kafka.yaml

# 本地构建时，可以切换成国内镜像：https://mirrors.cloud.tencent.com/apache/flink/flink-cdc-${FLINK_CDC_VERSION}/$TAR_FILE
# RUN wget https://archive.apache.org/dist/flink/flink-cdc-${FLINK_CDC_VERSION}/$TAR_FILE ; \
RUN wget https://mirrors.cloud.tencent.com/apache/flink/flink-cdc-${FLINK_CDC_VERSION}/$TAR_FILE ; \
    tar -zxf $TAR_FILE --strip 1 -C $FLINK_CDC_HOME ; \
    rm $TAR_FILE

RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-mysql/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-mysql-${FLINK_CDC_VERSION}.jar -O $FLINK_CDC_HOME/lib/flink-cdc-pipeline-connector-mysql.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-doris/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-doris-${FLINK_CDC_VERSION}.jar -O $FLINK_CDC_HOME/lib/flink-cdc-pipeline-connector-doris.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-starrocks/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-starrocks-${FLINK_CDC_VERSION}.jar -O $FLINK_CDC_HOME/lib/flink-cdc-pipeline-connector-starrocks.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-kafka/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-kafka-${FLINK_CDC_VERSION}.jar -O $FLINK_CDC_HOME/lib/flink-cdc-pipeline-connector-kafka.jar
RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-paimon/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-paimon-${FLINK_CDC_VERSION}.jar -O $FLINK_CDC_HOME/lib/flink-cdc-pipeline-connector-paimon.jar

RUN wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar -O $FLINK_CDC_HOME/lib/mysql-connector-java-8.0.27.jar

ENV CLASSPATH=$FLINK_HOME/lib/:$FLINK_CDC_LIB/*