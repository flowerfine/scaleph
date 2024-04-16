package cn.sliew.scaleph.application.flink.operator.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkSessionClusterSpec {

    /**
     * Flink docker image used to start the Job and TaskManager pods.
     */
    private String image;

    /**
     * Image pull policy of the Flink docker image.
     */
    private String imagePullPolicy;

    /**
     * Kubernetes service used by the Flink deployment.
     */
    private String serviceAccount;

    /**
     * Flink image version.
     */
    private FlinkVersion flinkVersion;

    /**
     * Ingress specs.
     */
    private IngressSpec ingress;

    /**
     * Base pod template for job and task manager pods. Can be overridden by the jobManager and
     * taskManager pod templates.
     */
    private Pod podTemplate;

    /**
     * JobManager specs.
     */
    private JobManagerSpec jobManager;

    /**
     * TaskManager specs.
     */
    private TaskManagerSpec taskManager;

    /**
     * Flink configuration overrides for the Flink deployment or Flink session job.
     */
    private Map<String, String> flinkConfiguration;

    /**
     * Log configuration overrides for the Flink deployment. Format logConfigFileName ->
     * configContent.
     */
    private Map<String, String> logConfiguration;

    /**
     * Deployment mode of the Flink cluster, native or standalone.
     */
    private KubernetesDeploymentMode mode;

    /**
     * Nonce used to manually trigger restart for the cluster/session job. In order to trigger
     * restart, change the number to anything other than the current value.
     */
    private Long restartNonce;

}
