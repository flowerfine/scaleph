import { Dict, QueryParam } from './app.data';

export class MetaDataSource {
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
}

export class MetaDataSourceParam extends QueryParam {
  datasourceName?: string;
  datasourceType?: string;
}

export const WORKBENCH_MENU = [
  {
    title: 'datadev.step.source',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.source-csv', menuIcon: 'icon-file', menuType: 'source', menuName: 'csv' },
      { title: 'datadev.step.source-excel', menuIcon: 'icon-file', menuType: 'source', menuName: 'excel' },
      { title: 'datadev.step.source-table', menuIcon: 'icon-table', menuType: 'source', menuName: 'table' },
      { title: 'datadev.step.source-mock', menuIcon: 'icon-modify-trace', menuType: 'source', menuName: 'mock' },
      { title: 'datadev.step.source-mockStream', menuIcon: 'icon-modify-trace', menuType: 'source', menuName: 'mockStream' },
      { title: 'datadev.step.source-kafka', menuIcon: 'icon-system', menuType: 'source', menuName: 'kafka' },
      { title: 'datadev.step.source-druid', menuIcon: 'icon-global-guide', menuType: 'source', menuName: 'druid' },
    ],
  },
  {
    title: 'datadev.step.sink',
    menuIcon: 'icon icon-folder',
    children: [
      { title: 'datadev.step.sink-csv', menuIcon: 'icon-file', menuType: 'sink', menuName: 'csv' },
      { title: 'datadev.step.sink-excel', menuIcon: 'icon-file', menuType: 'sink', menuName: 'excel' },
      { title: 'datadev.step.sink-table', menuIcon: 'icon-table', menuType: 'sink', menuName: 'table' },
      { title: 'datadev.step.sink-console', menuIcon: 'icon-console', menuType: 'sink', menuName: 'console' },
      { title: 'datadev.step.sink-kafka', menuIcon: 'icon-system', menuType: 'sink', menuName: 'kafka' },
      { title: 'datadev.step.sink-doris', menuIcon: 'icon-selct-template', menuType: 'sink', menuName: 'doris' },
      { title: 'datadev.step.sink-clickhouse', menuIcon: 'icon-date', menuType: 'sink', menuName: 'clickhouse' },
      { title: 'datadev.step.sink-elasticsearch', menuIcon: 'icon-json', menuType: 'sink', menuName: 'elasticsearch' },
      { title: 'datadev.step.sink-druid', menuIcon: 'icon-time-update', menuType: 'sink', menuName: 'druid' },
    ],
  },
  {
    title: 'datadev.step.trans',
    menuIcon: 'icon icon-folder',
    children: [
      {
        title: 'datadev.step.trans-field-select',
        menuIcon: 'icon-property',
        menuType: 'trans',
        menuName: 'field-select',
      },
      {
        title: 'datadev.step.trans-field-set-value',
        menuIcon: 'icon-set-keyword',
        menuType: 'trans',
        menuName: 'field-set-value',
      },
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
  partitionColumn: 'partition_column',
  batchSize: 'batch_size',
  bulkSize: 'bulk_size',
  preSQL: 'pre_sql',
  postSQL: 'post_sql',
  limit: 'limit',
  mockDataSize: 'mock_data_size',
  mockDataSchema: 'mock_data_schema',
  mockDataInterval: 'mock_data_interval',
  topics: 'topics',
  producerBootstrapServers: 'producer_bootstrap_servers',
  producerConf: 'producer_conf',
  semantic: 'semantic',
  consumerBootstrapServers: 'consumer_bootstrap_servers',
  consumerGroupId: 'consumer_group_id',
  consumerConf: 'consumer_conf',
  offsetReset: 'offset_reset',
  offsetResetSpecific: 'offset_reset_specific',
  rowtimeField: 'rowtime_field',
  watermark: 'watermark',
  formatType: 'format_type',
  formatConf: 'format_conf',
  schema: 'schema',
  table: 'table',
  interval: 'interval',
  maxRetries: 'max_retries',
  dorisConf: 'doris_conf',
  fields: 'fields',
  clickhouseConf: 'clickhouse_conf',
  index: 'index',
  indexTimeFormat: 'index_time_format',
  datasourceName: 'datasourceName',
  startDate: 'start_date',
  endDate: 'end_date',
  columns: 'columns',
  parallelism: 'parallelism',
  coordinatorUrl: 'coordinator_url',
  timestampColumn: 'timestamp_column',
  timestampFormat: 'timestamp_format',
  timestampMissingValue: 'timestamp_missing_value',
};

export class MockData {
  id?: number;
  name?: string;
  type?: any;
  minValue?: string;
  maxValue?: string;
  valueSeed?: string;
}

export const MOCK_DATA_TYPE = [
  { value: 'varchar', label: 'STRING' },
  { value: 'int', label: 'INTEGER' },
  { value: 'bigint', label: 'BIGINT' },
  { value: 'float', label: 'FLOAT' },
  { value: 'double', label: 'DOUBLE' },
  { value: 'boolean', label: 'BOOLEAN' },
  { value: 'timestamp', label: 'TIMESTAMP' },
];
