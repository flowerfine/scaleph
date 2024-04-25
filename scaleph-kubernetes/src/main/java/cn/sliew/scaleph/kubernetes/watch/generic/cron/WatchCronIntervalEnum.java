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

import lombok.Getter;

import java.time.Duration;

@Getter
public enum WatchCronIntervalEnum {

    LEVEL_5s(60L, Duration.ofSeconds(5L)),
    LEVEL_10s(120L, Duration.ofSeconds(10L)),
    LEVEL_30s(180L, Duration.ofSeconds(30L)),
    LEVEL_1m(240L, Duration.ofMinutes(1L)),
    LEVEL_2m(300L, Duration.ofMinutes(2L)),
    LEVEL_3m(360L, Duration.ofMinutes(3L)),
    LEVEL_4m(420L, Duration.ofMinutes(4L)),
    LEVEL_5m(Long.MAX_VALUE, Duration.ofMinutes(5L)),
    ;

    private Long maxCount;
    private Duration interval;

    WatchCronIntervalEnum(Long maxCount, Duration interval) {
        this.maxCount = maxCount;
        this.interval = interval;
    }
}
