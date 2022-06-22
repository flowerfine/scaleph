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




