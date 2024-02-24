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

ARG FLINK_VERSION=1.15
ARG BASE_RELEASE_IMAGE=flink:${FLINK_VERSION}
FROM $BASE_RELEASE_IMAGE as release

ARG SEATUNNEL_VERSION=2.3.4
ARG TAR_FILE=apache-seatunnel-${SEATUNNEL_VERSION}-bin.tar.gz

ENV SEATUNNEL_HOME=/opt/seatunnel
RUN mkdir -p $SEATUNNEL_HOME

RUN wget https://archive.apache.org/dist/seatunnel/${SEATUNNEL_VERSION}/$TAR_FILE ; \
    tar -zxf $TAR_FILE --strip 1 -C $SEATUNNEL_HOME ; \
    rm $TAR_FILE

# strange behavior: replace mvnw by mvn on install-plugin.sh, remove useless comment on plugin_config
RUN rm $SEATUNNEL_HOME/config/plugin_config
COPY tools/docker/build/scaleph-seatunnel/plugin_config $SEATUNNEL_HOME/config

RUN rm $SEATUNNEL_HOME/bin/install-plugin.sh
COPY tools/docker/build/scaleph-seatunnel/install-plugin.sh $SEATUNNEL_HOME/bin

RUN cd $SEATUNNEL_HOME ; \
    sh -x bin/install-plugin.sh ${SEATUNNEL_VERSION}

# RUN cd ${SEATUNNEL_HOME}||sh bin/install-plugin.sh ${SEATUNNEL_VERSION}

# BASE_RELEASE_IMAGE argument must be defined twice
#ARG FLINK_VERSION=1.15
#ARG BASE_RELEASE_IMAGE=flink:${FLINK_VERSION}
#FROM $BASE_RELEASE_IMAGE as release
#
#ENV SEATUNNEL_HOME=/opt/seatunnel
#
#RUN mkdir -p $SEATUNNEL_HOME
#
#COPY --from=build $SCALEPH_HOME $SCALEPH_HOME/
