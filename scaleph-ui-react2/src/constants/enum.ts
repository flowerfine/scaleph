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
