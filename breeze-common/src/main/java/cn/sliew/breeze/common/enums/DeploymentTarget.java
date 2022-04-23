package cn.sliew.breeze.common.enums;

import lombok.Getter;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.core.execution.PipelineExecutorFactory;
import org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;

@Getter
public enum DeploymentTarget {

    STANDALONE_APPLICATION(0, ResourceProvider.STANDALONE, DeploymentMode.APPLICATION, RemoteExecutor.NAME),
    STANDALONE_SESSION(1, ResourceProvider.STANDALONE, DeploymentMode.SESSION, RemoteExecutor.NAME),

    NATIVE_KUBERNETES_APPLICATION(2, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.APPLICATION, KubernetesDeploymentTarget.APPLICATION.getName()),
    NATIVE_KUBERNETES_SESSION(3, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.SESSION, KubernetesDeploymentTarget.SESSION.getName()),

    YARN_APPLICATION(4, ResourceProvider.YARN, DeploymentMode.APPLICATION, YarnDeploymentTarget.APPLICATION.getName()),
    YARN_PER_JOB(5, ResourceProvider.YARN, DeploymentMode.PER_JOB, YarnDeploymentTarget.PER_JOB.getName()),
    YARN_SESSION(6, ResourceProvider.YARN, DeploymentMode.SESSION, YarnDeploymentTarget.SESSION.getName()),
    ;

    private int code;
    private ResourceProvider resourceProvider;
    private DeploymentMode deploymentMode;

    /**
     * @see PipelineExecutorFactory#getName()
     */
    private String name;

    DeploymentTarget(int code, ResourceProvider resourceProvider, DeploymentMode deploymentMode, String name) {
        this.resourceProvider = resourceProvider;
        this.deploymentMode = deploymentMode;
        this.name = name;
    }

    public void apply(Configuration configuration) {
        configuration.set(DeploymentOptions.TARGET, getName());
    }
}
