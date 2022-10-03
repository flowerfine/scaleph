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
}

export const STEP_ATTR_TYPE = {
  jobGraph: 'jobGraph',
  jobId: 'jobId',
  stepCode: 'stepCode',
  stepTitle: 'stepTitle',
  stepAttrs: 'stepAttrs',
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
  rowNum: 'row_num',
  host: 'host',
  port: 'port',
};

export const BaseFileParams = {
  path: 'path',
  type: 'type',
  schema: 'schema',
  fileNameExpression: 'file_name_expression',
  fileFormat: 'file_format',
  filenameTimeFormat: 'filename_time_format',
  fieldDelimiter: 'field_delimiter',
  rowDelimiter: 'row_delimiter',
  partitionBy: 'partition_by',
  partitionDirExpression: 'partition_dir_expression',
  isPartitionFieldWriteInFile: 'is_partition_field_write_in_file',
  sinkColumns: 'sink_columns',
  isEnableTransaction: 'is_enable_transaction',
  saveMode: 'save_mode',
};

export const HDFSFileParams = {
  defaultFS: 'fs.defaultFS',
};

export const FtpFileParams = {
  host: 'host',
  port: 'port',
  username: 'username',
  password: 'password',
};

export const OSSFileParams = {
  endpoint: 'endpoint',
  bucket: 'bucket',
  accessKey: 'access_key',
  accessSecret: 'access_secret',
};

export const BaseHttpParams = {
  url: 'url',
  headers: 'headers',
  params: 'params',
  retry: 'retry',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  retryBackoffMaxMs: 'retry_backoff_max_ms'
}

export const HttpParams = {
  method: 'method',
  body: 'body',
  format: 'format',
  schema: 'schema',
  pollIntervalMs: 'poll_interval_ms'
}

export const WeChatParams = {
  url: 'url',
  mentionedList: 'mentioned_list',
  mentionedMobileList: 'mentioned_mobile_list'
}

export const FeishuParams = {
  url: 'url',
  headers: 'headers'
}

export const DingTalkParams = {
  url: 'url',
  secret: 'secret'
}

export const EmailParams = {
  emailHost: 'email_host',
  emailTransportProtocol: 'email_transport_protocol',
  emailFromAddress: 'email_from_address',
  emailSmtpAuth: 'email_smtp_auth',
  emailAuthorizationCode: 'email_authorization_code',
  emailToAddress: 'email_to_address',
  emailMessageHeadline: 'email_message_headline',
  emailMessageContent: 'email_message_content'
}

export const HudiParams = {
  tablePath: 'table.path',
  tableType: 'table.type',
  confFiles: 'conf.files',
  useKerberos: 'use.kerberos',
  kerberosPrincipal: 'kerberos.principal',
  kerberosPrincipalFile: 'kerberos.principal.file',
};

export const IcebergParams = {
  catalogType: 'catalog_type',
  catalogName: 'catalog_name',
  namespace: 'namespace',
  table: 'table',
  uri: 'uri',
  warehouse: 'warehouse',
  caseSensitive: 'case_sensitive',
  fields: 'fields',
  useSnapshotId: 'use_snapshot_id',
  startSnapshotId: 'start_snapshot_id',
  endSnapshotId: 'end_snapshot_id',
  startSnapshotTimestamp: 'start_snapshot_timestamp',
  useSnapshotTimestamp: 'use_snapshot_timestamp',
  streamScanStrategy: 'stream_scan_strategy',
}


export const ClickHouseParams = {
  dataSourceType: 'ClickHouse',
  splitMode: 'split_mode',
  shardingKey: 'sharding_key',
  clickhouseConf: 'clickhouse_conf',
  sql: 'sql',
  database: 'database'
};
