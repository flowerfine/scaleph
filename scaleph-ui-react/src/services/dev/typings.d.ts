import {Dict, QueryParam} from "@/app.d";
import {ClusterCredential, FlinkRelease} from "@/services/resource/typings";

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
  flinkArtifact: FlinkArtifact;
  version?: string;
  flinkVersion: Dict;
  entryClass: string;
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
  configOptions?: { [key: string]: any };
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkClusterConfigParam = QueryParam & {
  name?: string;
  flinkVersion?: string;
  resourceProvider?: string;
  deployMode?: string;
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

export type FlinkJobConfigJar = {
  id?: number;
  name?: string;
  flinkArtifactJar?: FlinkArtifactJar;
  flinkClusterConfig?: FlinkClusterConfig;
  flinkClusterInstance?: FlinkClusterInstance;
  jobConfig?: { [key: string]: any };
  flinkConfig?: { [key: string]: any };
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkJobConfigJarParam = QueryParam & {
  name?: string;
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
}
