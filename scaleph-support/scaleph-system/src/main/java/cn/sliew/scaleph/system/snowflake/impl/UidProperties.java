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

package cn.sliew.scaleph.system.snowflake.impl;

import cn.sliew.scaleph.system.snowflake.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * UID 的配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "uid")
public class UidProperties {

    /**
     * 时间增量值占用位数。当前时间相对于时间基点的增量值，单位为秒
     */
    private int timeBits = 30;

    /**
     * 工作机器ID占用的位数
     */
    private int workerBits = 16;

    /**
     * 序列号占用的位数
     */
    private int seqBits = 7;

    /**
     * 时间基点. 例如 2019-02-20 (毫秒: 1550592000000)
     */
    private String epochStr = "2019-02-20";

    /**
     * 时间基点对应的毫秒数
     */
    private long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1550592000000L);

    /**
     * 是否容忍时钟回拨, 默认:true
     */
    private boolean enableBackward = true;

    /**
     * 时钟回拨最长容忍时间（秒）
     */
    private long maxBackwardSeconds = 1L;

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }

}
