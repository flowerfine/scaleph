#!/usr/bin/env bash

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

HOME_DIR=$(
  cd "$(dirname $0)/.." || exit
  pwd
)
cd "${HOME_DIR}"
CONFIG_SCRIPT="${HOME_DIR}/bin/config.sh"
if [[ -f "${CONFIG_SCRIPT}" ]]; then
  source "${CONFIG_SCRIPT}"
fi
CLASSPATH="${HOME_DIR}/libs/*:${HOME_DIR}/conf"
JAVA_CMD="-cp ${CLASSPATH} cn.sliew.scaleph.Application"

if [[ -n "${JAVA_HOME}" ]]; then
  ${JAVA_HOME}/bin/java $JAVA_CMD
else
  java $JAVA_CMD
fi
