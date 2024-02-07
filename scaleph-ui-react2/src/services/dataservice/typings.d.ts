import {Dict, QueryParam} from "@/typings";

export type DataserviceConfig = {
  id?: number;
  projectId: number | string;
  name: string;
  path: string;
  method: string;
  contentType: string;
  status?: string;
  parameterMap?: DataserviceParameterMap;
  resultMap?: DataserviceResultMap;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DataserviceConfigParam = QueryParam & {
  name?: number;
}

export type DataserviceConfigSaveParam = {
  id?: number;
  projectId: number | string;
  name: string;
  path: string;
  method: string;
  contentType: string;
  parameterMappings?: Array<DataserviceParameterMapping>;
  resultMappings?: Array<DataserviceResultMapping>;
  type: string;
  query: string;
  remark?: string;
}

export type DataserviceParameterMap = {
  id?: number;
  projectId: number | string;
  name?: string;
  parameterMappings?: Array<DataserviceParameterMapping>
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DataserviceParameterMapping = {
  id?: number;
  parameterMapId?: number | string;
  property: string;
  javaType: string;
  jdbcType: string;
  typeHandler: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DataserviceResultMap = {
  id?: number;
  projectId: number | string;
  name?: string;
  resultMappings?: Array<DataserviceResultMapping>
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type DataserviceResultMapping = {
  id?: number;
  resultMapId?: number | string;
  property: string;
  javaType: string;
  jdbcType: string;
  typeHandler: string;
  createTime?: Date;
  updateTime?: Date;
}
