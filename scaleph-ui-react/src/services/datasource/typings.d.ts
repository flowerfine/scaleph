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

export type DsInfo = {
  id: number;
  dsTypeId: number;
  version?: string;
  name: string;
  props?: any;
  additionalProps?: any;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DsInfoParam = {
  dsTypeId?: number;
  name?: string;
}

export type DsInfoAddParam = {
  dsTypeId: number;
  version?: string;
  name: string;
  props?: any;
  additionalProps?: any;
  remark?: string;
}

export type DsInfoUpdateParam = {
  dsTypeId: number;
  version?: string;
  name: string;
  props?: any;
  additionalProps?: any;
  remark?: string;
}
