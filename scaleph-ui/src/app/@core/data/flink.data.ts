import {Dict, QueryParam} from './app.data';

export interface KeyValueConfig {
  key: string;
  value: string;
}

export class FlinkClusterConfig {
  id?: number;
  name?: string;
  flinkVersion?: Dict;
  resourceProvider?: Dict;
  deployMode?: Dict;
  flinkReleaseId?: number;
  deployConfigFileId?: number;
  configOptions?: { [key: string]: any };
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class FlinkClusterConfigParam extends QueryParam {
  name?: string;
  flinkVersion?: string;
  resourceProvider?: string;
  deployMode?: string;
}

export class FlinkClusterInstance {
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

export class FlinkClusterInstanceParam extends QueryParam {
  name?: string;
  flinkClusterConfigId?: number;
  status?: string;
}

export class FlinkSessionClusterAddParam extends QueryParam {
  flinkClusterConfigId?: number;
  remark?: string;
}


