import { Dict, QueryParam } from './app.data';

export class FlinkRelease {
  id?: number;
  version?: Dict;
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

export class SeaTunnelRelease {
  id?: number;
  version?: Dict;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class SeaTunnelReleaseParam extends QueryParam {
  version?: string;
  fileName?: string;
}

export class SeaTunnelReleaseUploadParam {
  version?: string;
  file?: File;
  remark?: string;
}

export class ClusterCredential {
  id?: number;
  configType?: Dict;
  name?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class ClusterCredentialParam extends QueryParam {
  configType?: string;
  name?: string;
}

export class ClusterCredentialUploadParam {
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
