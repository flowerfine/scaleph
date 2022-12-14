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

package cn.sliew.scaleph.workflow.scheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface SchedulerService {

    boolean exists(Long id);

    void schedule(Long id);

    void unschedule(Long id);

    void suspend(Long id);

    void resume(Long id);

    void terminate(Long id);

    List<Date> listNext5FireTime(String crontabStr) throws ParseException;

}
