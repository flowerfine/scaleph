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

ARG FLINK_CDC_VERSION=3.1.0
ARG TAR_FILE=flink-cdc-${FLINK_CDC_VERSION}-bin.tar.gz

ENV FLINK_CDC_HOME=/opt/flink-cdc
RUN mkdir -p $FLINK_CDC_HOME

# 本地构建时，可以切换成国内镜像：https://mirrors.cloud.tencent.com/apache/flink/flink-cdc-${FLINK_CDC_VERSION}/$TAR_FILE
RUN wget https://archive.apache.org/dist/flink/flink-cdc-${FLINK_CDC_VERSION}/$TAR_FILE ; \
    tar -zxf $TAR_FILE --strip 1 -C $FLINK_CDC_HOME ; \
    rm $TAR_FILE