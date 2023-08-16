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

# BASE_RELEASE_IMAGE argument must be defined twice
ARG FLINK_VERSION=1.15
ARG BASE_RELEASE_IMAGE=flink:${FLINK_VERSION}
FROM $BASE_RELEASE_IMAGE as release

ARG SEATUNNEL_VERSION=2.3.2
ENV SEATUNNEL_HOME=/opt/seatunnel

RUN mkdir -p $SEATUNNEL_HOME

COPY apache-seatunnel-${SEATUNNEL_VERSION}-bin/* $SCALEPH_HOME/
