package cn.sliew.scaleph.engine.flink.pipeline;

import cn.sliew.milky.common.chain.AbstractPipelineProcess;
import cn.sliew.milky.common.chain.Command;
import cn.sliew.milky.common.chain.Context;
import cn.sliew.milky.common.chain.PipelineException;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FlinkReleaseLoadCommand implements Command<String, Object> {

    public static final String NAME = "FlinkReleaseLoad";

    public static final String WORKSPACE = "workspace";
    public static final String FLINK_RELEASE_ID = "flinkReleaseId";

    public static final String FLINK_RELEASE_PATH = "flinkReleasePath";

    private final FlinkReleaseService flinkReleaseService;

    public FlinkReleaseLoadCommand(FlinkReleaseService flinkReleaseService) {
        this.flinkReleaseService = flinkReleaseService;
    }

    @Override
    public void onEvent(AbstractPipelineProcess<String, Object> process, Context<String, Object> context, CompletableFuture<?> future) {
        try {
            final Path workspace = (Path) context.get(WORKSPACE);
            final Long flinkReleaseId = (Long) context.get(FLINK_RELEASE_ID);
            final FlinkReleaseDTO flinkReleaseDTO = flinkReleaseService.selectOne(flinkReleaseId);
            final Path tempFile = TempFileUtil.createTempFile(workspace, flinkReleaseDTO.getFileName());
            try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
                flinkReleaseService.download(flinkReleaseDTO.getId(), outputStream);
            }
            final Path untarDir = TarUtil.untar(tempFile);
            final Path path = Files.list(untarDir).collect(Collectors.toList()).get(0);
            context.put(FLINK_RELEASE_PATH, path);
            process.fireEvent(context, future);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }

    @Override
    public void exceptionCaught(AbstractPipelineProcess<String, Object> process, Context<String, Object> context, CompletableFuture<?> future, Throwable cause) throws PipelineException {
        process.fireExceptionCaught(context, future, cause);
    }
}
