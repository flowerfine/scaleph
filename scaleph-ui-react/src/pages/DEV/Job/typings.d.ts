import {Dict, QueryParam} from "@/app.d";
import {FlinkArtifactJar, FlinkClusterConfig, FlinkClusterInstance} from "@/services/dev/typings";
import {DiJob} from "@/services/project/typings";

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
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkJobListParam = QueryParam & {
  type: string;
  name?: string;
}

export type FlinkJobListByCodeParam = QueryParam & {
  code: number;
}

export type FlinkJobListByTypeParam = QueryParam & {
  type: string;
  name?: string;
  flinkClusterConfigId?: number;
  flinkClusterInstanceId?: number;
}

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
  createTime?: Date;
  updateTime?: Date;
}

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
}

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
}

export type FlinkJobInstanceListParam = QueryParam & {
  flinkJobCode: number;
}

export type FlinkJobLog = FlinkJobInstance & {

}

export type FlinkJobLogListParam = QueryParam & {
  flinkJobCode: number;
}
