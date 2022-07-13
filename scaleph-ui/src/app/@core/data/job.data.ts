import { Dict, QueryParam } from './app.data';

export class FlinkArtifact {
  id?: number;
  name?: string;
  path?: string;
  entryClass?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class FlinkArtifactParam extends QueryParam {
  name?: string;
}

export class FlinkArtifactUploadParam {
  name?: string;
  entryClass?: string;
  file?: File;
  remark?: string;
}

export class FlinkJobConfig {
  id?: number;
  type?: Dict;
  name?: string;
  flinkClusterConfigId?: number;
  jobConfig?: object;
  flinkConfig?: object;
  version?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class FlinkJobConfigParam extends QueryParam {
  type?: string;
  name?: string;
  flinkClusterConfigId?: number;
}


