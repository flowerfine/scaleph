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

package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlinkReleaseLoadAction extends AbstractWorkFlow {

    public static final String NAME = FlinkReleaseLoadAction.class.getName();

    public static final AttributeKey<Long> FLINK_RELEASE_ID = AttributeKey.newInstance("flinkReleaseId");
    public static final AttributeKey<Path> FLINK_RELEASE_PATH = AttributeKey.newInstance("flinkReleasePath");

    private Path workspace;
    private FlinkReleaseService flinkReleaseService;

    public FlinkReleaseLoadAction(Path workspace, FlinkReleaseService flinkReleaseService) {
        super(NAME);
        this.workspace = workspace;
        this.flinkReleaseService = flinkReleaseService;
    }

    @Override
    public List<AttributeKey> getInputs() {
        return Arrays.asList(FLINK_RELEASE_ID);
    }

    @Override
    public List<AttributeKey> getOutputs() {
        return Arrays.asList(FLINK_RELEASE_PATH);
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> {
            try {
                download(context);
                listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
            } catch (IOException e) {
                listener.onFailure(e);
            }
        };
    }

    private void download(ActionContext context) throws IOException {
        Attribute<Long> flinkReleaseId = context.attr(FLINK_RELEASE_ID);
        FlinkReleaseDTO flinkReleaseDTO = flinkReleaseService.selectOne(flinkReleaseId.get());

        final Path tempFile = FileUtil.createTempFile(workspace, flinkReleaseDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
            flinkReleaseService.download(flinkReleaseDTO.getId(), outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        Path flinkReleasePath = Files.list(untarDir).collect(Collectors.toList()).get(0);
        Attribute<Path> flinkReleasePathAttr = context.attr(FLINK_RELEASE_PATH);
        flinkReleasePathAttr.setIfAbsent(flinkReleasePath);
    }

}
