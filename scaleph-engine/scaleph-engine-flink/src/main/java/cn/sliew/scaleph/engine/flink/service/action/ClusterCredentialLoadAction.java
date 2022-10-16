package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ClusterCredentialLoadAction extends AbstractWorkFlow {

    public static final String NAME = ClusterCredentialLoadAction.class.getName();

    public static final AttributeKey<Long> CLUSTER_CREDENTIAL_ID = AttributeKey.newInstance("clusterCredentialId");
    public static final AttributeKey<Path> CLUSTER_CREDENTIAL_PATH = AttributeKey.newInstance("clusterCredentialPath");

    private Path workspace;
    private ClusterCredentialService clusterCredentialService;

    public ClusterCredentialLoadAction(Path workspace, ClusterCredentialService clusterCredentialService) {
        super(NAME);
        this.workspace = workspace;
        this.clusterCredentialService = clusterCredentialService;
    }

    @Override
    public List<AttributeKey> getInputs() {
        return Arrays.asList(CLUSTER_CREDENTIAL_ID);
    }

    @Override
    public List<AttributeKey> getOutputs() {
        return Arrays.asList(CLUSTER_CREDENTIAL_PATH);
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
        Attribute<Long> clusterCredentialId = context.attr(CLUSTER_CREDENTIAL_ID);
        ClusterCredentialDTO clusterCredentialDTO = clusterCredentialService.selectOne(clusterCredentialId.get());
        List<FileStatusVO> fileStatusVOS = clusterCredentialService.listCredentialFile(clusterCredentialId.get());
        Path tempDir = FileUtil.createDir(workspace, clusterCredentialDTO.getName());
        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            Path deployConfigFile = FileUtil.createTempFile(tempDir,fileStatusVO.getName());
            try (OutputStream outputStream = FileUtil.getOutputStream(deployConfigFile)) {
                clusterCredentialService.downloadCredentialFile(clusterCredentialDTO.getId(), fileStatusVO.getName(), outputStream);
            }
        }
        Attribute<Path> clusterCredentialPath = context.attr(CLUSTER_CREDENTIAL_PATH);
        clusterCredentialPath.setIfAbsent(tempDir);
    }
}
