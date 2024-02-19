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

package cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint;

public class SavepointType implements SnapshotType {
    private final String name;
    private final PostCheckpointAction postCheckpointAction;
    private final SavepointFormatType formatType;

    private SavepointType(String name, PostCheckpointAction postCheckpointAction, SavepointFormatType formatType) {
        this.postCheckpointAction = postCheckpointAction;
        this.name = name;
        this.formatType = formatType;
    }

    public static SavepointType savepoint(SavepointFormatType formatType) {
        return new SavepointType("Savepoint", SavepointType.PostCheckpointAction.NONE, formatType);
    }

    public static SavepointType terminate(SavepointFormatType formatType) {
        return new SavepointType("Terminate Savepoint", SavepointType.PostCheckpointAction.TERMINATE, formatType);
    }

    public static SavepointType suspend(SavepointFormatType formatType) {
        return new SavepointType("Suspend Savepoint", SavepointType.PostCheckpointAction.SUSPEND, formatType);
    }

    public boolean isSavepoint() {
        return true;
    }

    public boolean isSynchronous() {
        return this.postCheckpointAction != SavepointType.PostCheckpointAction.NONE;
    }

    public PostCheckpointAction getPostCheckpointAction() {
        return this.postCheckpointAction;
    }

    public boolean shouldAdvanceToEndOfTime() {
        return this.shouldDrain();
    }

    public boolean shouldDrain() {
        return this.getPostCheckpointAction() == SavepointType.PostCheckpointAction.TERMINATE;
    }

    public boolean shouldIgnoreEndOfInput() {
        return this.getPostCheckpointAction() == SavepointType.PostCheckpointAction.SUSPEND;
    }

    public String getName() {
        return this.name;
    }

    public SavepointFormatType getFormatType() {
        return this.formatType;
    }

    public SnapshotType.SharingFilesStrategy getSharingFilesStrategy() {
        return SharingFilesStrategy.NO_SHARING;
    }

    public enum PostCheckpointAction {
        NONE, SUSPEND, TERMINATE;
    }
}
