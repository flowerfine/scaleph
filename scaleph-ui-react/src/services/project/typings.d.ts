import { Dict, QueryParam } from '@/app.d';
import { FlinkArtifactJar, FlinkClusterConfig, FlinkClusterInstance } from '@/services/dev/typings';

export type MetaDataSource = {
  id?: number;
  datasourceName?: string;
  datasourceType?: Dict;
  props?: any;
  propsStr?: string;
  additionalProps?: any;
  additionalPropsStr?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  passwdChanged?: boolean;
};

export type MetaDataSourceParam = QueryParam & {
  datasourceName?: string;
  datasourceType?: string;
};

export type DiProject = {
  id?: number;
  projectCode?: string;
  projectName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type DiProjectParam = QueryParam & {
  projectCode?: string;
  projectName?: string;
};


export type DiJob = {
  id?: number;
  projectId?: number;
  jobCode?: number;
  jobName?: string;
  jobType?: Dict;
  jobStatus?: Dict;
  jobVersion?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  jobAttrList?: DiJobAttr[];
  jobLinkList?: DiJobLink[];
  jobStepList?: DiJobStep[];
  jobGraph?: any;
};

export type DiJobParam = QueryParam & {
  projectId: string;
  jobCode?: number;
  jobName?: string;
  jobType?: string;
};

export type DiJobAddParam = {
  projectId?: number;
  jobName: string;
  jobType?: string;
  remark?: string;
};

export type DiJobUpdateParam = DiJobAddParam & {
  id?: number;
};

export type DiJobGraphParam = {
  jobId?: number;
  jobGraph?: any;
};

export type DiJobAttr = {
  id?: number;
  jobId: number;
  jobAttrType: Dict;
  jobAttrKey: string;
  jobAttrValue: string;
};

export type DiJobLink = {
  id?: number;
  jobId: number;
  linkCode: string;
  fromStepCode: string;
  toStepCode: string;
};

export type DiJobStep = {
  id?: number;
  jobId: number;
  stepCode: string;
  stepTitle: string;
  stepType: Dict;
  stepName: Dict;
  positionX: number;
  positionY: number;
  stepAttrs?:any;
  createTime?: Date;
  updateTime?: Date;
};


export type FlinkJob = {
  id?: number;
  type: string;
  code?: number;
  name?: string;
  flinkArtifactId?: number;
  jobConfig?: { [key: string]: any };
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
  flinkConfig?: { [key: string]: any };
  jars?: Array<number>;
  version?: number;
  remark?: string;
  creator: string;
  createTime?: Date;
  updateTime?: Date;
};

export type FlinkJobListParam = QueryParam & {
  type?: string;
  name?: string;
};

export type FlinkJobListByCodeParam = QueryParam & {
  code: number;
};

export type FlinkJobListByTypeParam = QueryParam & {
  type: string;
  name?: string;
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
};

export type FlinkJobForJar = {
  id?: number;
  type: Dict;
  code?: number;
  name?: string;
  flinkArtifactJar?: FlinkArtifactJar;
  jobConfig?: { [key: string]: any };
  flinkClusterConfig?: FlinkClusterConfig;
  flinkClusterInstance?: FlinkClusterInstance;
  flinkConfig?: { [key: string]: any };
  flinkJobInstance?: FlinkJobInstance;
  jars?: Array<number>;
  version?: number;
  remark?: string;
  creator?: string;
  createTime?: Date;
  editor?: string;
  updateTime?: Date;
};

export type FlinkJobForSeaTunnel = {
  id?: number;
  type: Dict;
  code?: number;
  name?: string;
  flinkArtifactSeaTunnel?: DiJob;
  jobConfig?: { [key: string]: any };
  flinkClusterConfig?: FlinkClusterConfig;
  flinkClusterInstance?: FlinkClusterInstance;
  flinkConfig?: { [key: string]: any };
  flinkJobInstance?: FlinkJobInstance;
  jars?: Array<number>;
  version?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type FlinkJobInstance = {
  id: number;
  type: Dict;
  flinkJobCode: number;
  flinkJobVersion: number;
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

export type FlinkJobInstanceListParam = QueryParam & {
  flinkJobCode: number;
};

export type FlinkJobLog = FlinkJobInstance & {};

export type FlinkJobLogListParam = QueryParam & {
  flinkJobCode: number;
};


export type FlinkArtifact = {
  id?: number;
  name?: string;
  type?: Dict;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkArtifactListParam = QueryParam & {
  name?: string;
  type?: string;
};

export type FlinkArtifactJar = {
  id?: number;
  flinkArtifact?: FlinkArtifact;
  version?: string;
  flinkVersion?: Dict;
  entryClass?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkArtifactJarListParam = QueryParam & {
  flinkArtifactId?: number;
};

export type FlinkArtifactJarUploadParam = QueryParam & {
  flinkArtifactId: number;
  version?: string;
  flinkVersion: string;
  entryClass?: string;
  file?: File;
};

export type FlinkClusterConfig = {
  id?: number;
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
}

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
}

export type FlinkClusterConfigParam = QueryParam & {
  name?: string;
  flinkVersion?: string;
  resourceProvider?: string;
  deployMode?: string;
}

export type FlinkClusterConfigAddParam = {
  name?: string;
  flinkVersion?: string;
  resourceProvider?: string;
  deployMode?: string;
  flinkReleaseId?: number;
  clusterCredentialId?: number;
  remark?: string;
}

export type FlinkClusterInstance = {
  id?: number;
  flinkClusterConfigId?: number;
  name?: string;
  clusterId?: string;
  webInterfaceUrl?: string;
  status?: Dict;
  remark?: number;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkClusterInstanceParam = QueryParam & {
  name?: string;
  flinkClusterConfigId?: number;
  status?: string;
}

export type FlinkSessionClusterNewParam = QueryParam & {
  flinkClusterConfigId?: number;
  remark?: string;
}
