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

package cn.sliew.scaleph.system.snowflake;

import cn.sliew.scaleph.system.snowflake.impl.CachedUidGenerator;
import cn.sliew.scaleph.system.snowflake.impl.DefaultUidGenerator;
import cn.sliew.scaleph.system.snowflake.impl.UidProperties;
import cn.sliew.scaleph.system.snowflake.worker.DisposableWorkerIdAssigner;
import cn.sliew.scaleph.system.snowflake.worker.WorkerIdAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ConditionalOnClass({ DefaultUidGenerator.class, CachedUidGenerator.class })
@EnableConfigurationProperties(UidProperties.class)
public class UidAutoConfigure {

	@Autowired
	private UidProperties uidProperties;

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	DefaultUidGenerator defaultUidGenerator() {
		return new DefaultUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	CachedUidGenerator cachedUidGenerator() {
		return new CachedUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	WorkerIdAssigner workerIdAssigner() {
		return new DisposableWorkerIdAssigner();
	}
}
