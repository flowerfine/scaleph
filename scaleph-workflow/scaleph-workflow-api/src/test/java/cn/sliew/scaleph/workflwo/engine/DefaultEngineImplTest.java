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

package cn.sliew.scaleph.workflwo.engine;

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.EngineBuilder;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultEngineImplTest {

    @Test
    void run() {
        Engine engine = EngineBuilder.newInstance().build();
        // given
        WorkFlow workFlow = Mockito.mock(WorkFlow.class);
        ActionContext context = Mockito.mock(ActionContext.class);
        ActionListener listener = Mockito.mock(ActionListener.class);

        // when
        Mockito.when(workFlow.getName()).thenReturn("mockito");
        engine.run(workFlow, context, listener);

        // then
        Mockito.verify(workFlow).execute(context, listener);
    }

}
