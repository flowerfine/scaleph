import {Dict, QueryParam} from "@/app.d";

export type DsCategory = {
  id: number;
  name?: string;
  order?: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DsType = {
  id?: number;
  type: Dict;
  logo: string;
  order: number;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DsTypeParam = QueryParam & {
  categoryId?: number;
  type?: string;
}
