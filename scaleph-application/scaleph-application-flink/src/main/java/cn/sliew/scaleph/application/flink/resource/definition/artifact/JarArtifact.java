package cn.sliew.scaleph.application.flink.resource.definition.artifact;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.kubernetes.DockerImage;
import lombok.Data;

import java.util.List;

@Data
public class JarArtifact implements Artifact {

    private String jarUri;
    private String entryClass;
    private String mainArgs;
    private FlinkVersion flinkVersion;
    private DockerImage dockerImage;
    private List<String> additionalDependencies;

    @Override
    public FlinkJobType getType() {
        return FlinkJobType.JAR;
    }

    @Override
    public FlinkVersion getFlinkVersion() {
        return flinkVersion;
    }

    @Override
    public DockerImage getDockerImage() {
        return dockerImage;
    }
}
