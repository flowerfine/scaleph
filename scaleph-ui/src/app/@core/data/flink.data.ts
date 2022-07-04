import { Dict, QueryParam } from './app.data';

export class FlinkRelease {
  id?: number;
  version?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class FlinkReleaseParam extends QueryParam {
  version?: string;
  fileName?: string;
}

export class FlinkReleaseUploadParam {
  version?: string;
  file?: File;
  remark?: string;
}

export class FlinkDeployConfig {
  id?: number;
  configType?: Dict;
  name?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class FlinkDeployConfigParam extends QueryParam {
  configType?: string;
  name?: string;
}

export class FlinkDeployConfigUploadParam {
  configType?: string;
  name?: string;
  files?: File[];
  remark?: string;
}

export class FileStatus {
  name?: string;
  len?: number;
  blockSize?: number;
  modificationTime?: Date;
  accessTime?: Date;
}

export class FlinkClusterConfig {
  id?: number;
  name?: string;
  flinkVersion?: Dict;
  resourceProvider?: Dict;
  deployMode?: Dict;
  flinkReleaseId?: number;
  deployConfigFileId?: number;
  configOptions?: object;
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


