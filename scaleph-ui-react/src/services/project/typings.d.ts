import {Dict, QueryParam} from '@/typings';
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

export type WsArtifact = {
  id?: number;
  projectId?: number | string;
  type?: Dict;
  name?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactSeaTunnel = {
  id?: number;
  artifact?: WsArtifact;
  seaTunnelEngine?: Dict;
  flinkVersion?: Dict;
  seaTunnelVersion?: Dict;
  dagId?: number;
  dag?: Dag;
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type Dag = {
  id?: number;
  dagMeta?: Record<string, any>;
  dagAttrs?: Record<string, any>;
  links?: Array<DagLink>;
  steps?: Array<DagStep>;
  createTime?: Date;
  updateTime?: Date;
};

export type DagLink = {
  id?: number;
  dagId?: number;
  linkId?: string;
  linkName?: string;
  fromStepId?: string;
  toStepId?: string;
  linkMeta?: Record<string, any>;
  linkAttrs?: Record<string, any>;
  createTime?: Date;
  updateTime?: Date;
};

export type DagStep = {
  id?: number;
  dagId?: number;
  stepId?: string;
  stepName?: string;
  positionX?: number;
  positionY?: string;
  stepMeta?: Record<string, any>;
  stepAttrs?: Record<string, any>;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactSeaTunnelParam = QueryParam & {
  projectId: number;
  flinkVersion?: string;
  seaTunnelVersion?: string;
  name?: string;
};

export type WsArtifactSeaTunnelHistoryParam = QueryParam & {
  artifactId: number;
};

export type WsArtifactSeaTunnelSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsArtifactSeaTunnelSaveParam = {
  id?: number;
  projectId?: number;
  name?: string;
  remark?: string;
};

export type WsArtifactSeaTunnelGraphParam = {
  id?: number;
  jobGraph?: any;
};

export type WsDiJob = {
  id?: number;
  wsFlinkArtifact?: WsArtifact;
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
  positionX: number;
  positionY: number;
  stepAttrs?: any;
  stepMeta?: any;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactFlinkCDC = {
  id?: number;
  artifact?: WsArtifact;
  flinkVersion?: Dict;
  flinkCDCVersion?: Dict;
  parallelism?: number;
  localTimeZone?: string;
  fromDsId?: number;
  fromDsConfig?: Record<string, any>;
  toDsId?: number;
  toDsConfig?: Record<string, any>;
  transform?: Array<Record<string, any>>;
  route?: Array<Record<string, any>>;
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactFlinkCDCParam = QueryParam & {
  projectId: number;
  flinkVersion?: string;
  flinkCDCVersion?: string;
  name?: string;
};

export type WsArtifactFlinkCDCHistoryParam = QueryParam & {
  artifactId: number;
};

export type WsArtifactFlinkCDCSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsArtifactFlinkCDCAddParam = {
  projectId: number;
  name?: string;
  parallelism?: number;
  localTimeZone?: string;
  fromDsId: number;
  fromDsConfig?: Record<string, any>;
  toDsId: number;
  toDsConfig?: Record<string, any>;
  transform?: Array<any>;
  route?: Array<any>;
  remark?: string;
};

export type WsArtifactFlinkCDCUpdateParam = {
  id: number;
  name?: string;
  remark?: string;
};

export type WsArtifactFlinkJar = {
  id?: number;
  artifact?: WsArtifact;
  flinkVersion?: Dict;
  entryClass?: string;
  fileName?: string;
  path?: string;
  jarParams?: { [key: string]: any };
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactFlinkJarUploadParam = {
  projectId: number | string;
  name: string;
  remark?: string;
  entryClass: string;
  flinkVersion: string;
  jarParams?: string;
  file?: UploadFile<any>;
};

export type WsArtifactFlinkJarUpdateParam = {
  id: number;
  name?: string;
  remark?: string;
  entryClass: string;
  flinkVersion: string;
  jarParams?: string;
  file?: UploadFile<any>;
};

export type WsArtifactFlinkJarParam = QueryParam & {
  projectId?: number | string;
  name?: string;
  flinkVersion: string;
};

export type WsArtifactFlinkJarHistoryParam = QueryParam & {
  artifactId: number;
};

export type WsArtifactFlinkJarSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsArtifactFlinkSql = {
  id?: number;
  artifact?: WsArtifact;
  flinkVersion?: Dict;
  script?: string;
  current?: Dict;
  createTime?: Date;
  updateTime?: Date;
};

export type WsArtifactFlinkSqlParam = QueryParam & {
  projectId?: number | string;
  name?: string;
  flinkVersion: string;
};

export type WsArtifactFlinkSqlHistoryParam = QueryParam & {
  artifactId: number;
};

export type WsArtifactFlinkSqlSelectListParam = {
  projectId: number;
  name?: string;
};

export type WsArtifactFlinkSqlSaveParam = {
  id?: number;
  projectId: number | string;
  name: string;
  remark?: string;
};

export type WsArtifactFlinkSqlScriptUpdateParam = {
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
  artifactFlinkJar?: WsArtifactFlinkJar;
  artifactFlinkSql?: WsArtifactFlinkSql;
  artifactFlinkCDC?: WsArtifactFlinkCDC;
  artifactSeaTunnel?: WsArtifactSeaTunnel;
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
  artifactFlinkJarId?: number;
  artifactFlinkSqlId?: number;
  artifactFlinkCDCId?: number;
  artifactSeaTunnelId?: number;
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
  mergedFlinkConfiguration?: Record<string, any>;
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
