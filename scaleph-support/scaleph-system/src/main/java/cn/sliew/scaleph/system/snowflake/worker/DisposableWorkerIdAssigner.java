/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.system.snowflake.worker;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.snowflake.SnowflakeWorkerNode;
import cn.sliew.scaleph.dao.mapper.master.snowflake.SnowflakeWorkerNodeMapper;
import cn.sliew.scaleph.system.snowflake.utils.DockerUtils;
import cn.sliew.scaleph.system.snowflake.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 */
@Slf4j
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    @Resource
    private SnowflakeWorkerNodeMapper workerNodeDAO;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public long assignWorkerId() {
        // build worker node entity
        SnowflakeWorkerNode workerNodeEntity = buildWorkerNode();

        SnowflakeWorkerNode oldWorkerNode = workerNodeDAO
                .getWorkerNodeByHostPort(workerNodeEntity.getHostName(), workerNodeEntity.getPort());
        if (null != oldWorkerNode) {
            return oldWorkerNode.getId();
        }

        // add worker node for new (ignore the same IP + PORT)
        workerNodeDAO.insert(workerNodeEntity);
        log.info("Add snowflake worker node: {}", JacksonUtil.toJsonString(workerNodeEntity));

        return workerNodeEntity.getId();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public long assignFakeWorkerId() {
        return buildFakeWorkerNode().getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private SnowflakeWorkerNode buildWorkerNode() {
        SnowflakeWorkerNode workerNodeEntity = new SnowflakeWorkerNode();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.getType());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());
        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.getType());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(0, 100000));
        }

        return workerNodeEntity;
    }

    private SnowflakeWorkerNode buildFakeWorkerNode() {
        SnowflakeWorkerNode workerNodeEntity = new SnowflakeWorkerNode();
        workerNodeEntity.setType(WorkerNodeType.FAKE.getType());
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort() + "-" + RandomUtils.nextInt(0, 100000));
        } else {
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(0, 100000));
        }
        return workerNodeEntity;
    }
}
