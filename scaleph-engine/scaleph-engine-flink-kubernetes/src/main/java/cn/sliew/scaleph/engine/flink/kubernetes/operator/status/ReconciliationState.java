package cn.sliew.scaleph.engine.flink.kubernetes.operator.status;

/**
 * Current state of the reconciliation.
 */
public enum ReconciliationState {

    /**
     * The lastReconciledSpec is currently deployed.
     */
    DEPLOYED,
    /**
     * The spec is being upgraded.
     */
    UPGRADING,
    /**
     * In the process of rolling back to the lastStableSpec.
     */
    ROLLING_BACK,
    /**
     * Rolled back to the lastStableSpec.
     */
    ROLLED_BACK
}
