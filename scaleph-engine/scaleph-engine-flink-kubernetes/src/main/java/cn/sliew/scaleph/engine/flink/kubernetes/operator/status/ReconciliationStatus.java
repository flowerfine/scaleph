package cn.sliew.scaleph.engine.flink.kubernetes.operator.status;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.AbstractFlinkSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.util.SpecUtils;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.util.SpecWithMeta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Status of the last reconcile step for the FlinkDeployment/FlinkSessionJob.
 */
@Data
@NoArgsConstructor
public abstract class ReconciliationStatus<SPEC extends AbstractFlinkSpec> {

    /**
     * Epoch timestamp of the last successful reconcile operation.
     */
    private long reconciliationTimestamp;

    /**
     * Last reconciled deployment spec. Used to decide whether further reconciliation steps are
     * necessary.
     */
    private String lastReconciledSpec;

    /**
     * Last stable deployment spec according to the specified stability condition. If a rollback
     * strategy is defined this will be the target to roll back to.
     */
    private String lastStableSpec;

    /**
     * Deployment state of the last reconciled spec.
     */
    private ReconciliationState state = ReconciliationState.UPGRADING;

    @JsonIgnore
    public abstract Class<SPEC> getSpecClass();

    @JsonIgnore
    public SPEC deserializeLastReconciledSpec() {
        var specWithMeta = deserializeLastReconciledSpecWithMeta();
        return specWithMeta != null ? specWithMeta.getSpec() : null;
    }

    @JsonIgnore
    public SPEC deserializeLastStableSpec() {
        var specWithMeta = deserializeLastStableSpecWithMeta();
        return specWithMeta != null ? specWithMeta.getSpec() : null;
    }

    @JsonIgnore
    public SpecWithMeta<SPEC> deserializeLastReconciledSpecWithMeta() {
        return SpecUtils.deserializeSpecWithMeta(lastReconciledSpec, getSpecClass());
    }

    @JsonIgnore
    public SpecWithMeta<SPEC> deserializeLastStableSpecWithMeta() {
        return SpecUtils.deserializeSpecWithMeta(lastStableSpec, getSpecClass());
    }

    @JsonIgnore
    public void serializeAndSetLastReconciledSpec(
            SPEC spec, AbstractFlinkResource<SPEC, ?> resource) {
        setLastReconciledSpec(SpecUtils.writeSpecWithMeta(spec, resource));
    }

    public void markReconciledSpecAsStable() {
        lastStableSpec = lastReconciledSpec;
    }

    @JsonIgnore
    public boolean isLastReconciledSpecStable() {
        if (lastReconciledSpec == null || lastStableSpec == null) {
            return false;
        }
        return lastReconciledSpec.equals(lastStableSpec);
    }

    @JsonIgnore
    public boolean isBeforeFirstDeployment() {
        return lastReconciledSpec == null;
    }
}
