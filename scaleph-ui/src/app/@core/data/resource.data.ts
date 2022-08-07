import { Dict, QueryParam } from './app.data';

export class ReleaseFlink {
  id?: number;
  version?: Dict;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class ReleaseFlinkParam extends QueryParam {
  version?: string;
  fileName?: string;
}

export class ReleaseFlinkUploadParam {
  version?: string;
  file?: File;
  remark?: string;
}

export class ReleaseSeaTunnel {
  id?: number;
  version?: Dict;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class ReleaseSeaTunnelParam extends QueryParam {
  version?: string;
  fileName?: string;
}

export class ReleaseSeaTunnelUploadParam {
  version?: string;
  file?: File;
  remark?: string;
}
