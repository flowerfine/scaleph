import {Dict, QueryParam} from "@/app";

export class FlinkArtifact {
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
