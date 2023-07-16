package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.instance;

import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;

public interface ArtifactHandler extends DeploymentKindCapable, FlinkJobTypeCapable {

    void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, Object spec);

}
