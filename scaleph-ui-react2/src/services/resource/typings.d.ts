import {Dict, QueryParam} from "@/app";

export type ResourceListParam = QueryParam & {
  resourceType?: string;
  label?: string;
  name?: string;
}

export type Jar = {
  id?: number;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type JarListParam = QueryParam & {
  fileName?: string;
}

export type JarUploadParam = QueryParam & {
  file: File;
  remark?: String;
}

export type FlinkRelease = {
  id?: number;
  version?: Dict;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type FlinkReleaseListParam = QueryParam & {
  version?: string;
  fileName?: string;
}

export type FlinkReleaseUploadParam = QueryParam & {
  version?: string;
  file: File;
  remark?: string;
}

export type SeaTunnelRelease = {
  id?: number;
  version?: Dict;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type SeaTunnelReleaseListParam = QueryParam & {
  version?: string;
  fileName?: string;
}

export type SeaTunnelReleaseUploadParam = QueryParam & {
  version?: string;
  file: File;
  remark?: String;
}

export type SeaTunnelConnectorUploadParam = QueryParam & {
  id: string;
  pluginName: string;
  file: File;
}

export type SeaTunnelConnectorFile = {
  name: string;
  len: number;
  blockSize: number;
  modificationTime: Date;
  accessTime: Date;
}

export type ClusterCredential = {
  id?: number;
  name?: string;
  context?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type ClusterCredentialListParam = QueryParam & {
  name?: string;
}

export type ClusterCredentialUploadParam = QueryParam & {
  name: string;
  context?: string;
  file: File;
  remark?: string;
}

export type CredentialFile = {
  name?: string;
  len?: number;
  blockSize?: number;
  modificationTime?: Date;
  accessTime?: Date;
}

export type CredentialFileUploadParam = QueryParam & {
  id?: number;
  files?: File[];
}
