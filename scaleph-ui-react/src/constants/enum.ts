export enum DeploymentKind {
  Deployment = 'FlinkDeployment',
  SessionCluster = 'FlinkSessionJob'
}

export enum YesOrNo {
  YES = '1',
  NO = '0'
}

export enum FlinkJobType {
  JAR = '0',
  SQL = '1',
  SEATUNNEL = '2',
  FLINK_CDC = '3'
}

export enum ResourceLifecycleState {
  CREATED = 'CREATED',
  SUSPENDED = 'SUSPENDED',
  UPGRADING = 'UPGRADING',
  DEPLOYED = 'DEPLOYED',
  STABLE = 'STABLE',
  ROLLING_BACK = 'ROLLING_BACK',
  ROLLED_BACK = 'ROLLED_BACK',
  FAILED = 'FAILED'
}

export enum FlinkJobState {
  INITIALIZING = 'INITIALIZING',
  CREATED = 'CREATED',
  RUNNING = 'RUNNING',
  FAILING = 'FAILING',
  FAILED = 'FAILED',
  CANCELLING = 'CANCELLING',
  CANCELED = 'CANCELED',
  FINISHED = 'FINISHED',
  RESTARTING = 'RESTARTING',
  SUSPENDED = 'SUSPENDED',
  RECONCILING = 'RECONCILING',
}
