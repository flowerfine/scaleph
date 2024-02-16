package cn.sliew.scaleph.workspace.flink.kubernetes.resource.definition.job.instance;

import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.workspace.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.workspace.flink.kubernetes.operator.spec.JobSpec;
import cn.sliew.scaleph.workspace.flink.kubernetes.resource.handler.FileFetcherHandler;
import cn.sliew.scaleph.workspace.flink.kubernetes.resource.handler.SeaTunnelConfHandler;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class FlinkDeploymentArtifactHandler implements ArtifactHandler {

    @Autowired
    private FileFetcherHandler fileFetcherHandler;
    @Autowired
    private SeaTunnelConfHandler seaTunnelConfHandler;

    @Override
    public boolean support(DeploymentKind deploymentKind) {
        return deploymentKind == DeploymentKind.FLINK_DEPLOYMENT;
    }

    public void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, Object spec) {
        FlinkDeploymentSpec flinkDeploymentSpec = (FlinkDeploymentSpec) spec;
        switch (jobInstanceDTO.getWsFlinkKubernetesJob().getType()) {
            case JAR:
                addJarArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            case SQL:
                addSQLArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            case SEATUNNEL:
                addSeaTunnelArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            default:
        }
    }

    private void addJarArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        WsFlinkArtifactJar flinkArtifactJar = jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkArtifactJar();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.JAR_LOCAL_PATH + flinkArtifactJar.getFileName());
        jobSpec.setEntryClass(flinkArtifactJar.getEntryClass());
        jobSpec.setArgs(StringUtils.split(flinkArtifactJar.getJarParams(), " "));
        spec.setJob(jobSpec);
        fileFetcherHandler.handleJarArtifact(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void addSQLArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        WsFlinkArtifactSql flinkArtifactSql = jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkArtifactSql();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.SQL_RUNNER_LOCAL_PATH);
        jobSpec.setEntryClass(ResourceNames.SQL_RUNNER_ENTRY_CLASS);
        List<String> args = Arrays.asList(SqlUtil.format(flinkArtifactSql.getScript()));
        jobSpec.setArgs(args.toArray(new String[1]));
        spec.setJob(jobSpec);
    }

    private void addSeaTunnelArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.SEATUNNEL_STARTER_PATH + SeaTunnelReleaseUtil.STARTER_JAR_NAME);
        jobSpec.setEntryClass(SeaTunnelReleaseUtil.SEATUNNEL_MAIN_CLASS);
        List<String> args = Arrays.asList("--config", ResourceNames.SEATUNNEL_CONF_FILE_PATH);
        jobSpec.setArgs(args.toArray(new String[2]));
        spec.setJob(jobSpec);
        seaTunnelConfHandler.handle(jobInstanceDTO, spec);
    }
}
