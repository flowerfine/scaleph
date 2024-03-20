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

package cn.sliew.scaleph.common.concurrent;

public interface RunnableWrapper extends Runnable {

    @Override
    default void run() {
        try {
            onBefore();
            doRun();
        } catch (Exception t) {
            onFailure(t);
        } finally {
            onAfter();
        }
    }

    /**
     * This method has the same semantics as {@link Runnable#run()}
     *
     * @throws InterruptedException if the run method throws an InterruptedException
     */
    void doRun() throws Exception;

    /**
     * This method is invoked for all exception thrown by {@link #doRun()}
     */
    void onFailure(Exception e);

    /**
     * This method is called before all execution for init.
     */
    default void onBefore() throws Exception {

    }

    /**
     * This method is called in a finally block after successful execution
     * or on a rejection.
     */
    default void onAfter() {
        // nothing by default
    }
}
