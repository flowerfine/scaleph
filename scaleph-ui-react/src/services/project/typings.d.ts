import {Dict, QueryParam} from '@/app.d';
import {UploadFile} from 'antd';

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

export type KubernetesOptionsVO = {
  image?: string;
  imagePullPolicy?: string;
  flinkVersion?: string;
  serviceAccount?: string;
};

export type WsFlinkKubernetesTemplate = {
  id?: number;
  projectId: number;
  name?: string;
  deploymentKind?: Dict;
  namespace: string;
  kubernetesOptions?: KubernetesOptionsVO;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  podTemplate?: Record<string, any>;
  flinkConfiguration?: Record<string, any>;
  logConfiguration?: Record<string, any>;
  ingress?: Record<string, any>;
  additionalDependencies?: Array<number>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesTemplateParam = QueryParam & {
  projectId: number;
  name?: string;
  deploymentKind?: string;
};

export type WsFlinkKubernetesTemplateAddParam = {
  projectId: number;
  name: string;
  deploymentKind?: string;
  namespace: string;
  kubernetesOptions?: KubernetesOptionsVO;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  podTemplate?: Record<string, any>;
  flinkConfiguration?: Record<string, any>;
  logConfiguration?: Record<string, any>;
  ingress?: Record<string, any>;
  additionalDependencies?: Array<number>;
  remark?: string;
};

export type WsFlinkKubernetesTemplateUpdateParam = {
  id: number;
  name: string;
  deploymentKind?: string;
  namespace: string;
  remark?: string;
};

export type WsFlinkKubernetesSessionCluster = {
  id?: number;
  projectId: number;
  clusterCredentialId: number;
  name: string;
  sessionClusterId: string;
  namespace: string;
  kubernetesOptions?: KubernetesOptionsVO;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  podTemplate?: Record<string, any>;
  flinkConfiguration?: Record<string, any>;
  logConfiguration?: Record<string, any>;
  ingress?: Record<string, any>;
  additionalDependencies?: Array<number>;
  deployed?: Dict;
  supportSqlGateway?: Dict;
  state?: Dict;
  error?: Dict;
  clusterInfo?: Record<string, any>;
  taskManagerInfo?: Record<string, any>;
  remark?: string;
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

export type WsFlinkKubernetesDeployment = {
  id?: number;
  projectId: number;
  clusterCredentialId: number;
  name: string;
  namespace: string;
  kubernetesOptions?: KubernetesOptionsVO;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  podTemplate?: Record<string, any>;
  flinkConfiguration?: Record<string, any>;
  logConfiguration?: Record<string, any>;
  ingress?: Record<string, any>;
  additionalDependencies?: Array<number>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesDeploymentParam = QueryParam & {
  projectId: number;
  clusterCredentialId?: number;
  name?: string;
};

export type WsFlinkKubernetesDeploymentSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsFlinkKubernetesJob = {
  id?: number;
  projectId: number;
  name: string;
  jobId?: string;
  executionMode: Dict;
  deploymentKind: Dict;
  flinkDeployment?: WsFlinkKubernetesDeployment;
  flinkSessionCluster?: WsFlinkKubernetesSessionCluster;
  type: Dict;
  flinkArtifactJar?: WsFlinkArtifactJar;
  flinkArtifactSql?: WsFlinkArtifactSql;
  wsDiJob?: WsDiJob;
  jobInstance?: WsFlinkKubernetesJobInstance;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesJobParam = QueryParam & {
  projectId: number;
  executionMode?: string;
  deploymentKind?: string;
  type?: string;
  state?: string;
  name?: string;
};

export type WsFlinkKubernetesJobAddParam = {
  projectId: number;
  name: string;
  executionMode: string;
  deploymentKind: string;
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

export type WsFlinkKubernetesJobInstance = {
  id: number;
  wsFlinkKubernetesJobId: number;
  instanceId: string;
  parallelism?: number;
  upgradeMode?: Dict;
  allowNonRestoredState?: boolean;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  userFlinkConfiguration?: Record<string, any>;
  state: Dict;
  jobState?: Dict;
  error?: Dict;
  clusterInfo?: Record<string, any>;
  taskManagerInfo?: Record<string, any>;
  startTime?: Date;
  endTime?: Date;
  duration?: number;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesJobInstanceParam = QueryParam & {
  wsFlinkKubernetesJobId: number
};

export type WsFlinkKubernetesJobInstanceDeployParam = {
  wsFlinkKubernetesJobId: number;
  parallelism?: number;
  jobManager?: Record<string, any>;
  taskManager?: Record<string, any>;
  upgradeMode?: string;
  allowNonRestoredState?: boolean;
  userFlinkConfiguration?: Record<string, any>;
};

export type WsFlinkKubernetesJobInstanceShutdownParam = {
  id: number;
  savepoint?: boolean;
  drain?: boolean;
};

export type CatalogFunctionInfo = {
  functionName: string;
  functionKind: string
}

export type WsFlinkKubernetesJobInstanceSavepoint = {
  id: number;
  wsFlinkKubernetesJobInstanceId: number;
  timeStamp: number;
  location: string;
  triggerType: Dict;
  formatType: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsFlinkKubernetesJobInstanceSavepointParam = QueryParam & {
  wsFlinkKubernetesJobInstanceId: number
};

export type WsDorisOperatorTemplate = {
  id?: number;
  projectId: number;
  name?: string;
  templateId?: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsDorisOperatorTemplateParam = QueryParam & {
  projectId: number;
  name?: string;
};

export type WsDorisOperatorTemplateAddParam = {
  projectId: number;
  name: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  remark?: string;
};

export type WsDorisOperatorTemplateUpdateParam = {
  id: number;
  name: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  remark?: string;
};

export type WsDorisOperatorInstance = {
  id?: number;
  projectId: number;
  clusterCredentialId?: number;
  name?: string;
  instanceId?: string;
  namespace: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  deployed?: Dict;
  feStatus?: Record<string, any>;
  beStatus?: Record<string, any>;
  cnStatus?: Record<string, any>;
  brokerStatus?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsDorisOperatorInstanceParam = QueryParam & {
  projectId: number;
  name?: string;
};

export type WsDorisOperatorInstanceAddParam = {
  projectId: number;
  clusterCredentialId?: number;
  name: string;
  namespace: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  remark?: string;
};

export type WsDorisOperatorInstanceUpdateParam = {
  id: number;
  clusterCredentialId?: number;
  name: string;
  namespace: string;
  admin?: Record<string, any>;
  feSpec?: Record<string, any>;
  beSpec?: Record<string, any>;
  cnSpec?: Record<string, any>;
  brokerSpec?: Record<string, any>;
  remark?: string;
};

export type DorisClusterFeEndpoint = {
  http?: string;
  rpc?: string;
  query?: string;
  editLog?: string;
};
