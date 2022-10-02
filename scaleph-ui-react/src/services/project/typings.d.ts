import { Dict, QueryParam } from '@/app.d';

export type MetaDataSource = {
  id?: number;
  datasourceName?: string;
  datasourceType?: Dict;
  props?: any;
  propsStr?: string;
  additionalProps?: any;
  additionalPropsStr?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
  passwdChanged?: boolean;
};

export type MetaDataSourceParam = QueryParam & {
  datasourceName?: string;
  datasourceType?: string;
};

export type DiProject = {
  id?: number;
  projectCode?: string;
  projectName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
};

export type DiProjectParam = QueryParam & {
  projectCode?: string;
  projectName?: string;
};

export type DiDirectory = {
  id?: number;
  projectId?: string;
  directoryName?: string;
  pid?: number;
  fullPath?: string;
};

export type DiDirectoryTreeNode = {
  id: number;
  pid: number;
  directoryName: string;
  children: DiDirectoryTreeNode[];
};

export type DiJob = {
  id?: number;
  projectId?: string;
  jobCode?: string;
  jobName?: string;
  directory?: DiDirectory;
  jobType?: Dict;
  jobStatus?: Dict;
  runtimeState?: Dict;
  jobVersion?: number;
  remark?: string;
  jobCrontab?: string;
  createTime?: Date;
  updateTime?: Date;
  jobAttrList?: DiJobAttr[];
  jobLinkList?: DiJobLink[];
  jobStepList?: DiJobStep[];
  jobGraph?: any;
};

export type DiJobParam = QueryParam & {
  projectId: string;
  jobCode?: string;
  jobName?: string;
  jobType?: string;
  runtimeState?: string;
  directoryId?: string;
};

export type DiJobAttr = {
  id?: number;
  jobId: number;
  jobAttrType: Dict;
  jobAttrKey: string;
  jobAttrValue: string;
};

export type DiJobLink = {
  id?: number;
  jobId: number;
  linkCode: string;
  fromStepCode: string;
  toStepCode: string;
};

export type DiJobStep = {
  id?: number;
  jobId: number;
  stepCode: string;
  stepTitle: string;
  stepType: Dict;
  stepName: string;
  positionX: number;
  positionY: number;
  stepAttrs?:any;
  createTime?: Date;
  updateTime?: Date;
  jobStepAttrList: DiJobStepAttr[];
};

export type DiJobStepAttr = {
  id?: number;
  jobId: number;
  stepCode: string;
  stepAttrKey: string;
  stepAttrValue: string;
};
