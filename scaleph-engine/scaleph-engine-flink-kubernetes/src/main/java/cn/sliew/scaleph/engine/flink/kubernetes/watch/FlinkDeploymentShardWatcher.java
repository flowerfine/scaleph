package cn.sliew.scaleph.engine.flink.kubernetes.watch;

import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.kubernetes.watch.KubernetesSharedInformer;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.Informable;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkArgument;

public class FlinkDeploymentShardWatcher extends KubernetesSharedInformer<FlinkDeployment> {

    public FlinkDeploymentShardWatcher(NamespacedKubernetesClient client, Map<String, String> labels) {
        super(client, getInformableConfigMaps(client, labels));
    }

    private static Informable<FlinkDeployment> getInformableConfigMaps(
            NamespacedKubernetesClient client, Map<String, String> labels) {
        checkArgument(CollectionUtils.isEmpty(labels) == false, () -> "Labels must not be null or empty");
        return client.resources(FlinkDeployment.class).withLabels(labels);
    }
}
