import {Dict, QueryParam} from "@/app";
import {ClusterCredential, FlinkRelease} from "@/services/resource/typings";

export type FlinkArtifact = {
  id?: number;
  name?: string;
  path?: string;
  entryClass?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkArtifactListParam = QueryParam & {
  name?: string;
};

export type FlinkArtifactUploadParam = QueryParam & {
  name?: string;
  entryClass?: string;
  file?: File;
  remark?: string;
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
  flinkArtifact?: FlinkArtifact;
  flinkClusterConfig?: FlinkClusterConfig;
  flinkClusterInstance?: FlinkClusterInstance;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkJobConfigJarParam = QueryParam & {
  name?: string;
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
}
