import { Dict, QueryParam } from '@/app.d';
import { FlinkArtifactJar, FlinkClusterConfig, FlinkClusterInstance } from '@/services/dev/typings';
import { UploadFile } from 'antd';
import { ClusterCredential, FlinkRelease } from '@/services/resource/typings';

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

export type WsDiJob = {
  id?: number;
  projectId?: number | string;
  jobCode?: number;
  jobName?: string;
  jobType?: Dict | any;
  jobStatus?: Dict;
  jobVersion?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  jobAttrList?: WsDiJobAttr[];
  jobLinkList?: WsDiJobLink[];
  jobStepList?: WsDiJobStep[];
  jobGraph?: any;
};

export type WsDiJobParam = QueryParam & {
  projectId: string;
  jobCode?: number;
  jobName?: string;
  jobType?: string;
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

export type WsFlinkJobListByCodeParam = QueryParam & {
  code: number;
};

export type WsFlinkJobListByTypeParam = QueryParam & {
  type: string;
  name?: string;
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
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

export type WsFlinkJobInstanceListParam = QueryParam & {
  flinkJobCode: number;
};

export type WsFlinkJobLog = WsFlinkJobInstance & {};

export type WsFlinkJobLogListParam = QueryParam & {
  flinkJobCode: number;
};

export type WsFlinkArtifact = {
  id?: number;
  projectId?: number | string;
  name?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkArtifactListParam = QueryParam & {
  projectId?: number | string;
  name?: string;
};

export type WsFlinkArtifactJar = {
  id?: number;
  wsFlinkArtifact?: WsFlinkArtifact;
  flinkVersion?: Dict;
  entryClass?: string;
  fileName?: string;
  path?: string;
  version?: string;
  jarParams?: { [key: string]: any };
  file?: UploadFile<any>;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkArtifactJarParam = QueryParam & {
  id?: number | string;
  flinkArtifactId: number;
  version?: string;
  flinkVersion: string;
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

export type WsFlinkKubernetesDeploymentTemplate = {
  id?: number;
  name?: string;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesDeploymentTemplateParam = QueryParam & {
  name?: string;
};

export type WsFlinkKubernetesDeployment = {
  id?: number;
  name?: string;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesDeploymentParam = QueryParam & {
  name?: string;
};
