package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import org.apache.flink.configuration.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public abstract class ConfigurationBuildAction extends AbstractWorkFlow {

    private AttributeKey<Long> FLINK_CLUSTER_CONFIG_ID = AttributeKey.newInstance("flinkClusterConfigId");
    private AttributeKey<Configuration> CONFIGURATION = AttributeKey.newInstance("configuration");

    private FlinkClusterConfigService flinkClusterConfigService;

    public ConfigurationBuildAction(String name, FlinkClusterConfigService flinkClusterConfigService) {
        super(name);
        this.flinkClusterConfigService = flinkClusterConfigService;
    }

    @Override
    public List<AttributeKey> getInputs() {
        return Arrays.asList(FLINK_CLUSTER_CONFIG_ID);
    }

    @Override
    public List<AttributeKey> getOutputs() {
        return Arrays.asList(CONFIGURATION);
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> {
            try {
                build(context);
                listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
            } catch (IOException e) {
                listener.onFailure(e);
            }
        };
    }

    private void build(ActionContext context) throws IOException {
        Attribute<Long> flinkClusterConfigId = context.attr(FLINK_CLUSTER_CONFIG_ID);
        FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkClusterConfigId.get());
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(flinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(flinkClusterConfigDTO.getConfigOptions());
        }

        Attribute<Path> clusterCredentialPath = context.attr(ClusterCredentialLoadAction.CLUSTER_CREDENTIAL_PATH);

        Configuration configuration = buildForResource(context, clusterCredentialPath.get(), dynamicProperties);
        
        Attribute<Configuration> configurationAttr = context.attr(CONFIGURATION);
        configurationAttr.setIfAbsent(configuration);
    }

    protected abstract Configuration buildForResource(ActionContext context, Path clusterCredentialPath, Configuration dynamicProperties) throws IOException;
}
