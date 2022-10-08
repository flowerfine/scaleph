package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.TaskManagerOptions;
//import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationBuildForKubernetesAction extends ConfigurationBuildAction{

    public static final String NAME = ConfigurationBuildForKubernetesAction.class.getName();

    public ConfigurationBuildForKubernetesAction(FlinkClusterConfigService flinkClusterConfigService) {
        super(NAME, flinkClusterConfigService);
    }

    @Override
    protected Configuration buildForResource(ActionContext context, Path clusterCredentialPath,  Configuration dynamicProperties) throws IOException {
        List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childs)) {
            return dynamicProperties;
        }
        final Path kubeConfigFile = childs.get(0);
//        dynamicProperties.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, kubeConfigFile.toAbsolutePath().toString());
        if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }
}
