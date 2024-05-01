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

FROM eclipse-temurin:11-focal as release

ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=123456
ENV MINIO_ENDPOINT=http://minio:9000
ENV MINIO_ACCESS_KEY=admin
ENV MINIO_SECRET_KEY=password

ENV TZ Asia/Shanghai
ENV SCALEPH_HOME /opt/scaleph-file-fetcher

RUN mkdir -p $SCALEPH_HOME
WORKDIR $SCALEPH_HOME

ADD ./scaleph-file-fetcher/target/scaleph-file-fetcher*.tar.gz /opt/

ENV CLASSPATH $CLASSPATH:$SCALEPH_HOME/libs/*:$SCALEPH_HOME/conf/*

# shell script
ENTRYPOINT ["sh","-c","java cn.sliew.scaleph.Application \"$0\" \"$@\" --file-system.endPoint=$MINIO_ENDPOINT --file-system.accessKey=$MINIO_ACCESS_KEY --file-system.secretKey=$MINIO_SECRET_KEY"]