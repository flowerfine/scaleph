import { Dict, QueryParam } from './app.data';

export class FlinkRelease {
  id?: number;
  version?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  passwdChanged?: boolean;
}

export class FlinkReleaseParam extends QueryParam {
  version?: string;
  fileName?: string;
}


