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


