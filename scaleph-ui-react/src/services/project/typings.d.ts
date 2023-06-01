import {Dict, QueryParam} from '@/app.d';
import {UploadFile} from 'antd';
import {ClusterCredential, FlinkRelease} from '@/services/resource/typings';

export type WsProject = {
  id?: number;
  projectCode?: string;
  projectName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsProjectParam = QueryParam & {
  projectCode?: string;
  projectName?: string;
};

export type WsFlinkArtifact = {
  id?: number;
  projectId?: number | string;
  type?: Dict;
  name?: string;
  current?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkArtifactListParam = QueryParam & {
  projectId?: number | string;
  name?: string;
};

export type WsDiJob = {
  id?: number;
  wsFlinkArtifact?: WsFlinkArtifact;
  jobEngine?: Dict;
  jobId?: string;
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
  jobAttrList?: WsDiJobAttr[];
  jobLinkList?: WsDiJobLink[];
  jobStepList?: WsDiJobStep[];
  jobGraph?: any;
};

export type WsDiJobParam = QueryParam & {
  projectId: number;
  jobEngine?: string;
  name?: string;
};


export type WsDiJobSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsDiJobAddParam = {
  projectId: number;
  name?: string;
  jobEngine?: string;
  remark?: string;
};

export type WsDiJobGraphParam = {
  jobId?: number;
  jobGraph?: any;
};

export type WsDiJobAttr = {
  id?: number;
  jobId: number;
  jobAttrType: Dict;
  jobAttrKey: string;
  jobAttrValue: string;
};

export type WsDiJobLink = {
  id?: number;
  jobId: number;
  linkCode: string;
  fromStepCode: string;
  toStepCode: string;
};

export type WsDiJobStep = {
  id?: number;
  jobId: number;
  stepCode: string;
  stepTitle: string;
  stepType: Dict;
  stepName: Dict;
  positionX: number;
  positionY: number;
  stepAttrs?: any;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkJob = {
  id?: number;
  type: Dict;
  projectId: number | string;
  code?: number;
  name?: string;
  flinkArtifactId?: number;
  jobConfig?: { [key: string]: any };
  wsFlinkJobInstance?: WsFlinkJobInstance;
  wsFlinkClusterConfig?: WsFlinkClusterConfig;
  wsFlinkClusterInstance?: WsFlinkClusterInstance;
  flinkConfig?: { [key: string]: any };
  jars?: Array<number>;
  version?: number;
  remark?: string;
  creator?: string;
  createTime?: Date;
  editor?: string;
  updateTime?: Date;
};

export type WsFlinkJobListParam = QueryParam & {
  type?: string;
  name?: string;
  projectId?: string | number;
  flinkJobState?: string;
};

export type WsFlinkJobInstance = {
  id: number;
  type: Dict;
  flinkJobCode: number;
  jobId: string;
  jobName: string;
  jobState: Dict;
  clusterId: string;
  webInterfaceUrl: string;
  clusterStatus: Dict;
  startTime?: Date;
  endTime?: Date;
  duration?: number;
  createTime: Date;
  updateTime: Date;
};

export type WsFlinkJobLog = WsFlinkJobInstance & {};

export type WsFlinkJobLogListParam = QueryParam & {
  flinkJobCode: number;
};

export type WsFlinkArtifactJar = {
  id?: number;
  wsFlinkArtifact?: WsFlinkArtifact;
  flinkVersion?: Dict;
  entryClass?: string;
  fileName?: string;
  path?: string;
  jarParams?: { [key: string]: any };
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkArtifactJarUploadParam = {
  projectId: number | string;
  name: string;
  remark?: string;
  entryClass: string;
  flinkVersion: string;
  jarParams?: string;
  file?: UploadFile<any>;
};

export type WsFlinkArtifactJarUpdateParam = {
  id: number;
  name?: string;
  remark?: string;
  entryClass: string;
  flinkVersion: string;
  jarParams?: string;
  file?: UploadFile<any>;
};

export type WsFlinkArtifactJarParam = QueryParam & {
  id?: number | string;
  flinkArtifactId: number;
  version?: string;
  flinkVersion: string;
};

export type WsFlinkArtifactJarHistoryParam = QueryParam & {
  flinkArtifactId: number;
};

export type WsFlinkArtifactJarSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsFlinkArtifactSql = {
  id?: number;
  wsFlinkArtifact?: WsFlinkArtifact;
  flinkVersion?: Dict;
  script?: string;
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkArtifactSqlParam = QueryParam & {
  flinkArtifactId: number;
  version?: string;
  flinkVersion: string;
};

export type WsFlinkArtifactSqlSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsFlinkArtifactSqlAddParam = {
  projectId: number | string;
  name: string;
  remark?: string;
  flinkVersion: string;
};

export type WsFlinkArtifactSqlScriptUpdateParam = {
  id: number;
  script?: string;
};

export type WsFlinkClusterConfig = {
  id?: number;
  projectId?: number | string;
  name?: string;
  flinkVersion?: Dict;
  resourceProvider?: Dict;
  deployMode?: Dict;
  flinkRelease?: FlinkRelease;
  clusterCredential?: ClusterCredential;
  kubernetesOptions?: KubernetesOptions;
  configOptions?: { [key: string]: any };
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type KubernetesOptions = {
  context?: string;
  namespace?: string;
  serviceAccount?: string;
  registry?: string;
  repository?: string;
  image?: string;
  imagePullPolicy?: string;
  jobManagerCPU?: number;
  jobManagerMemory?: string;
  jobManagerReplicas?: number;
  taskManagerCPU?: number;
  taskManagerMemory?: string;
  taskManagerReplicas?: number;
};

export type WsFlinkClusterConfigParam = QueryParam & {
  projectId?: number | string;
  name?: string;
  flinkVersion?: string;
  resourceProvider?: string;
  deployMode?: string;
};

export type WsFlinkClusterInstance = {
  id?: number;
  flinkClusterConfigId?: number;
  name?: string;
  clusterId?: string;
  webInterfaceUrl?: string;
  status?: Dict;
  remark?: number;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkClusterInstanceParam = QueryParam & {
  projectId?: string | number;
  name?: string;
  flinkClusterConfigId?: number;
  status?: string;
};

export type WsFlinkCheckPoint = {
  id: number | string;
  flinkJobInstanceId?: number;
  flinkCheckpointId?: number;
  checkpointType?: Dict;
  status?: Dict;
  savepoint?: boolean;
  triggerTimestamp?: number;
  duration?: number;
  discarded?: boolean;
  externalPath?: string;
  stateSize?: number;
  processedData?: number;
  persistedData?: number;
  alignmentBuffered?: number;
  numSubtasks?: number;
  numAcknowledgedSubtasks?: number;
  latestAckTimestamp?: number;
};

export type WsFlinkCheckPointParam = QueryParam & {
  flinkJobInstanceId: number;
};

export type WsFlinkKubernetesTemplate = {
  id?: number;
  projectId: number;
  name?: string;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesTemplateParam = QueryParam & {
  projectId: number;
  name?: string;
};

export type WsFlinkKubernetesDeployment = {
  id?: number;
  projectId: number;
  kind: Dict;
  name: string;
  namespace: string;
  kuberenetesOptions?: KubernetesOptions;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  podTemplate?: Record<string, any>;
  flinkConfiguration?: Record<string, any>;
  logConfiguration?: Record<string, any>;
  ingress?: Record<string, any>;
  deploymentName?: string;
  job?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type KubernetesOptions = {
  image?: string;
  flinkVersion?: string;
  serviceAccount?: string;
};

export type WsFlinkKubernetesDeploymentParam = QueryParam & {
  projectId: number;
  kind: string;
  name?: string;
};

export type WsFlinkKubernetesDeploymentSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsFlinkKubernetesSessionCluster = {
  id?: number;
  projectId: number;
  clusterCredentialId: number;
  name: string;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesSessionClusterParam = QueryParam & {
  projectId: number;
  clusterCredentialId?: number;
  name?: string;
};

export type WsFlinkKubernetesSessionClusterSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsFlinkKubernetesJob = {
  id?: number;
  projectId: number;
  name: string;
  jobId?: string;
  executionMode: Dict;
  flinkDeploymentMode: Dict;
  flinkDeployment?: WsFlinkKubernetesDeployment;
  flinkSessionCluster?: WsFlinkKubernetesSessionCluster;
  type: Dict;
  flinkArtifactJar?: WsFlinkArtifactJar;
  flinkArtifactSql?: WsFlinkArtifactSql;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesJobParam = QueryParam & {
  projectId: number;
  executionMode?: string;
  flinkDeploymentMode?: string;
  type?: string;
  state?: string;
  name?: string;
};

export type WsFlinkKubernetesJobAddParam = {
  projectId: number;
  name: string;
  executionMode: string;
  flinkDeploymentMode: string;
  flinkDeploymentId?: number;
  flinkSessionClusterId?: number;
  type: string;
  flinkArtifactJarId?: number;
  flinkArtifactSqlId?: number;
  remark?: string;
};

export type WsFlinkKubernetesJobUpdateParam = {
  id: number;
  name: string;
  executionMode: string;
  remark?: string;
};
