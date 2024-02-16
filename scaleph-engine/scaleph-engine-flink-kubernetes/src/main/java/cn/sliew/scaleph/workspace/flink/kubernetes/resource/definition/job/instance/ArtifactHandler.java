package cn.sliew.scaleph.workspace.flink.kubernetes.resource.definition.job.instance;

import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;

public interface ArtifactHandler extends DeploymentKindCapable, FlinkJobTypeCapable {

    void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, Object spec);

}
