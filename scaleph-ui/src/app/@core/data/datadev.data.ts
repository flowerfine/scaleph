import { Dict, QueryParam } from './app.data';

export class DataSourceMeta {
  id?: number;
  dataSourceName?: string;
  dataSourceType?: Dict;
  connectionType?: Dict;
  hostName?: string;
  databaseName?: string;
  port?: number;
  userName?: string;
  password?: string;
  remark?: string;
  generalProps?: string;
  jdbcProps?: string;
  // poolProps?: string;
  passwdChanged?: boolean;
  createTime?: Date;
  updateTime?: Date;
}

export class DataSourceMetaParam extends QueryParam {
  dataSourceName?: string;
  dataSourceType?: string;
  hostName?: string;
  databaseName?: string;
}


export const WORKBENCH_MENU = [
  {
    title: 'datadev.step.source',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.source-csv', menuIcon: 'icon-file', menuType: 'source', menuName: 'csv' },
      { title: 'datadev.step.source-excel', menuIcon: 'icon-file', menuType: 'source', menuName: 'excel' },
      { title: 'datadev.step.source-table', menuIcon: 'icon-table', menuType: 'source', menuName: 'table' },
    ],
  },
  {
    title: 'datadev.step.sink',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.sink-csv', menuIcon: 'icon-file', menuType: 'sink', menuName: 'csv' },
      { title: 'datadev.step.sink-excel', menuIcon: 'icon-file', menuType: 'sink', menuName: 'excel' },
      { title: 'datadev.step.sink-table', menuIcon: 'icon-table', menuType: 'sink', menuName: 'table' },
    ],
  },
  {
    title: 'datadev.step.trans',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.trans-field-select', menuIcon: 'icon-property', menuType: 'trans', menuName: 'field-select' },
      { title: 'datadev.step.trans-field-set-value', menuIcon: 'icon-set-keyword', menuType: 'trans', menuName: 'field-set-value' },
      { title: 'datadev.step.trans-group', menuIcon: 'icon-groupby', menuType: 'trans', menuName: 'group' },
    ],
  },
  {
    title: 'datadev.step.flow',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.trans-filter', menuIcon: 'icon-filter-o', menuType: 'trans', menuName: 'filter' },
      { title: 'datadev.step.trans-case', menuIcon: 'icon-switch', menuType: 'trans', menuName: 'case' },
    ],
  },
];

export class DiProject {
  id?: number;
  projectCode?: string;
  projectName?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class DiProjectParam extends QueryParam {
  projectCode: string;
  projectName?: string;
}

export class DiResourceFile {
  id?: number;
  projectId?: number;
  projectCode?: string;
  fileName?: string;
  fileType?: string;
  filePath?: string;
  fileSize?: number;
  createTime?: Date;
  updateTime?: Date;
}

export class DiResourceFileParam extends QueryParam {
  projectId?: string;
  fileName?: string;
}

export class DiClusterConfig {
  id?: number;
  clusterName: string;
  clusterType?: string;
  clusterHome?: string;
  clusterVersion?: string;
  clusterConf?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export class DiClusterConfigParam extends QueryParam {
  clusterName?: string;
  clusterType?: string;
}

export class DiDirectory {
  id?: number;
  projectId?: number;
  directoryName: string;
  pid?: number;
  fullPath?: string;
}

export class DiJob {
  id?: number;
  projectId?: number;
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
}

export class DiJobParam extends QueryParam {
  projectId: number;
  jobCode: string;
  jobName: string;
  jobType: string;
  runtimeState: string;
  directoryId?: string;
}

export class DiJobAttr {
  id?: number;
  jobId: number;
  jobAttrType: Dict;
  jobAttrKey: string;
  jobAttrValue: string;
}

export class DiJobLink {
  id?: number;
  jobId: number;
  linkCode: string;
  fromStepCode: string;
  toStepCode: string;
}

export class DiJobStep {
  id?: number;
  jobId: number;
  stepCode: string;
  stepTitle: string;
  stepType: Dict;
  stepName: string;
  positionX: number;
  positionY: number;
  jobStepAttrList: DiJobStepAttr[];
}

export class DiJobStepAttr {
  id?: number;
  jobId: number;
  stepCode: string;
  stepAttrKey: string;
  stepAttrValue: string;
}

export class DiJobStepAttrType {
  id?: number;
  stepType: string;
  stepName: string;
  stepAttrKey: string;
  stepAttrDefaultValue: string;
  isRequired: string;
  stepAttrDescribe: string;
}

export const STEP_ATTR_TYPE = {
  jobGraph: 'jobGraph',
  jobId: 'jobId',
  stepCode: 'stepCode',
  stepTitle: 'stepTitle',
  dataSourceType: 'dataSourceType',
  dataSource: 'dataSource',
  query: 'query',
  batchSize: 'batchSize',
};
