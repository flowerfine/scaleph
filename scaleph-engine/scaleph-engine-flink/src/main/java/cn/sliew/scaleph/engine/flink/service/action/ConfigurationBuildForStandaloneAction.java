package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationBuildForStandaloneAction extends ConfigurationBuildAction {

    public static final String NAME = ConfigurationBuildForStandaloneAction.class.getName();

    public ConfigurationBuildForStandaloneAction(FlinkClusterConfigService flinkClusterConfigService) {
        super(NAME, flinkClusterConfigService);
    }

    @Override
    protected Configuration buildForResource(ActionContext context, Path clusterCredentialPath, Configuration dynamicProperties) throws IOException {
        final List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childs)) {
            return dynamicProperties;
        }
        return GlobalConfiguration.loadConfiguration(clusterCredentialPath.toString(), dynamicProperties);
    }
}
