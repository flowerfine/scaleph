import { Dict, QueryParam } from './app.data';

export const WORKBENCH_MENU = [
  {
    title: 'studio.step.source',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'studio.step.source-csv', menuIcon: 'icon-file', menuType: 'source', menuName: 'csv' },
      { title: 'studio.step.source-excel', menuIcon: 'icon-file', menuType: 'source', menuName: 'excel' },
      { title: 'studio.step.source-table', menuIcon: 'icon-table', menuType: 'source', menuName: 'table' },
    ],
  },
  {
    title: 'studio.step.sink',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'studio.step.sink-csv', menuIcon: 'icon-file', menuType: 'sink', menuName: 'csv' },
      { title: 'studio.step.sink-excel', menuIcon: 'icon-file', menuType: 'sink', menuName: 'excel' },
      { title: 'studio.step.sink-table', menuIcon: 'icon-table', menuType: 'sink', menuName: 'table' },
    ],
  },
  {
    title: 'studio.step.trans',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'studio.step.trans-field-select', menuIcon: 'icon-property', menuType: 'trans', menuName: 'field-select' },
      { title: 'studio.step.trans-field-set-value', menuIcon: 'icon-set-keyword', menuType: 'trans', menuName: 'field-set-value' },
      { title: 'studio.step.trans-group', menuIcon: 'icon-groupby', menuType: 'trans', menuName: 'group' },
    ],
  },
  {
    title: 'studio.step.flow',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'studio.step.trans-filter', menuIcon: 'icon-filter-o', menuType: 'trans', menuName: 'filter' },
      { title: 'studio.step.trans-case', menuIcon: 'icon-switch', menuType: 'trans', menuName: 'case' },
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
  projectId: number;
  jobCode: string;
  jobName: string;
  directory: DiDirectory;
  jobType?: Dict;
  jobStatus?: Dict;
  runtimeState?: Dict;
  jobVersion?: number;
  remark?: string;
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
