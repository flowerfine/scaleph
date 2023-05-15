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

function print_help() {
  echo "Usage:"
  echo "  $0 init-database"
  echo "Options:"
  echo "  --driver      Driver class name. required"
  echo "  --url         Database url. required"
  echo "  --username    Database username."
  echo "  --password    Database password."
  echo "  --sql-files   Directory of sql files stored. required"
  echo "For example: "
  echo "$0 init-database \\
  --driver com.mysql.cj.jdbc.Driver \\
  --url jdbc:mysql://127.0.0.1:3306 \\
  --username root \\
  --password 123456 \\
  --sql-files sql"
}

function init_database() {
  local JAVA_CMD="java"
  if [[ -n "${JAVA_HOME}" ]]; then
    JAVA_CMD="${JAVA_HOME}/bin/java"
  fi
  local CLASSPATH="${HOME_DIR}/libs/*:${HOME_DIR}/conf"
  local JAVA_OPT="-cp ${CLASSPATH}"
  exec ${JAVA_CMD} ${JAVA_OPT} "${HOME_DIR}/bin/DbTool.java" $@
}

case $1 in
init-database)
  init_database $@
  ;;
*)
  print_help
  ;;
esac