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

package cn.sliew.scaleph.kubernetes.watch.generic.cron;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CronManager {

    @Autowired(required = false)
    private List<KubernetesCronHandler> cronHandlers;

    private void checkStatus(WatchCronIntervalEnum intervalEnum) {
//        log.info("check status");
    }

    /**
     * quartz 配置为集群模式，这里无需添加分布式锁保证调度任务不重复执行
     */
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void refresh5s() {
        checkStatus(WatchCronIntervalEnum.LEVEL_5s);
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void refresh10s() {
        checkStatus(WatchCronIntervalEnum.LEVEL_10s);
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void refresh30s() {
        checkStatus(WatchCronIntervalEnum.LEVEL_30s);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void refresh1m() {
        checkStatus(WatchCronIntervalEnum.LEVEL_1m);
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void refresh2m() {
        checkStatus(WatchCronIntervalEnum.LEVEL_2m);
    }

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.MINUTES)
    public void refresh3m() {
        checkStatus(WatchCronIntervalEnum.LEVEL_3m);
    }

    @Scheduled(fixedDelay = 4, timeUnit = TimeUnit.MINUTES)
    public void refresh4m() {
        checkStatus(WatchCronIntervalEnum.LEVEL_4m);
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void refresh5m() {
        checkStatus(WatchCronIntervalEnum.LEVEL_5m);
    }
}
