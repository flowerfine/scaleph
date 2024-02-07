import {Dict, QueryParam} from "@/typings";

export type DataserviceConfig = {
  id?: number;
  projectId: number | string;
  name: string;
  path: string;
  method: string;
  contentType: string;
  status?: string;
  parameterMap?: Record<string, any>;
  resultMap?: Record<string, any>;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DataserviceConfigParam = QueryParam & {
  categoryId?: number;
  type?: string;
}
