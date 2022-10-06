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

ARG BASE_IMAGE=adoptopenjdk/openjdk11:jre

FROM $BASE_IMAGE

ARG FLINK_VERSION
ARG SCALA_VERSION
ARG TAR_FILE=flink-${FLINK_VERSION}-bin-scala_${SCALA_VERSION}.tgz

ENV FLINK_HOME /opt/flink

# download from flink release page
RUN mkdir -p $FLINK_HOME ; cd $FLINK_HOME ; \
    curl -LSO https://archive.apache.org/dist/flink/flink-${FLINK_VERSION}/$TAR_FILE ; \
    tar -zxf $TAR_FILE --strip 1 -C . ; \
    rm $TAR_FILE

# copy pre-downloaded flink release
#RUN mkdir -p $FLINK_HOME
#COPY /path/to/$TAR_FILE $FLINK_HOME/$TAR_FILE
#RUN cd $FLINK_HOME ;\
#    tar -zxf $TAR_FILE --strip 1 -C . ; \
#    rm $TAR_FILE