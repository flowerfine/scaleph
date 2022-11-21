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

package cn.sliew.scaleph.common.container.pool.thread;

import cn.sliew.scaleph.common.container.Container;
import cn.sliew.scaleph.common.container.pool.ContainerPool;
import cn.sliew.scaleph.common.container.pool.ContainerValue;

public class ThreadContainerPool implements ContainerPool {

    private ThreadContainerSource source = new ThreadContainerSource();

    @Override
    public ContainerValue obtain() {
        return new DefaultContainerValue(source.newInstance(), false);
    }

    public class DefaultContainerValue implements ContainerValue {

        Container value;
        final boolean recycled;

        DefaultContainerValue(Container value, boolean recycled) {
            this.value = value;
            this.recycled = recycled;
        }


        @Override
        public Container value() {
            return value;
        }

        @Override
        public boolean isRecycled() {
            return recycled;
        }

        @Override
        public void close() {
            if (value == null) {
                throw new IllegalStateException("recycler entry already released...");
            }
            source.recycle(value);
            value = null;
        }
    }
}
