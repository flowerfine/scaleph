package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import org.apache.flink.configuration.*;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigurationBuildForYarnAction extends ConfigurationBuildAction{

    public static final String NAME = ConfigurationBuildForYarnAction.class.getName();

    public ConfigurationBuildForYarnAction(FlinkClusterConfigService flinkClusterConfigService) {
        super(NAME, flinkClusterConfigService);
    }

    @Override
    protected Configuration buildForResource(ActionContext context, Path clusterCredentialPath,  Configuration dynamicProperties) throws IOException {
        dynamicProperties.set(CoreOptions.FLINK_HADOOP_CONF_DIR, clusterCredentialPath.toAbsolutePath().toString());
        if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }
}
