import { IGraphCommand, XFlowGraphCommands, XFlowNodeCommands } from '@antv/xflow';

export const DND_RENDER_ID = 'DND_NODE';
export const GROUP_NODE_RENDER_ID = 'GROUP_NODE_RENDER_ID';
export const EDGE_NODE_RENDER_ID = 'EDGE_NODE_RENDER_ID';
export const NODE_WIDTH = 180;
export const NODE_HEIGHT = 32;
export const ZOOM_OPTIONS = { maxScale: 2, minScale: 0.5 };
export const CONNECTION_PORT_TYPE = { source: 'outPort', target: 'inPort' };

/** custom commands */
export namespace CustomCommands {
  export const GRAPH_CUT: IGraphCommand = {
    id: 'xflow:graph-cut-selection',
    label: 'cut',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };

  export const NODE_EDIT: IGraphCommand = {
    id: 'xflow:node-edit',
    label: 'edit',
    category: XFlowNodeCommands.UPDATE_NODE.category,
  };

  export const GRAPH_PARAMS_SETTING: IGraphCommand = {
    id: 'xflow:graph-params-setting',
    label: 'graph-params',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };

  export const GRAPH_PUBLISH: IGraphCommand = {
    id: 'xflow:graph-publish',
    label: 'publish',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };

  // export const GRAPH_START: IGraphCommand = {
  //   id: 'xflow:graph-start',
  //   label: 'start',
  //   category: XFlowGraphCommands.GRAPH_COPY.category,
  // };

  // export const GRAPH_STOP: IGraphCommand = {
  //   id: 'xflow:graph-stop',
  //   label: 'stop',
  //   category: XFlowGraphCommands.GRAPH_COPY.category,
  // };
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

export const HudiParams = {
  tablePath: 'table.path',
  tableType: 'table.type',
  confFiles: 'conf.files',
  useKerberos: 'use.kerberos',
  kerberosPrincipal: 'kerberos.principal',
  kerberosPrincipalFile: 'kerberos.principal.file',
}


