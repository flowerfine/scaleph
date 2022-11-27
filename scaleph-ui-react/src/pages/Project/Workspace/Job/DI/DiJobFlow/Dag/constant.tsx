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

  export const GRAPH_PREVIEW: IGraphCommand = {
    id: 'xflow:graph-preview',
    label: 'graph-preview',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };

  export const GRAPH_PUBLISH: IGraphCommand = {
    id: 'xflow:graph-publish',
    label: 'publish',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };

  export const GRAPH_SUBMIT: IGraphCommand = {
    id: 'xflow:graph-submit',
    label: 'graph-submit',
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
  bulkSize: 'bulk_size',
  table: 'table',
  maxRetries: 'max_retries',
  fields: 'fields',
  datasourceName: 'datasourceName',
  host: 'host',
  port: 'port',
};

export const SchemaParams = {
  schema: 'schema',
  fields: 'fields',
  field: 'field',
  type: 'type',
  delimiter: 'delimiter',
}

export const FakeParams = {
  rowNum: 'row_num',
}

export const JdbcParams = {
  connectionCheckTimeoutSec: 'connection_check_timeout_sec',
  query: 'query',
  partitionColumn: 'partition_column',
  partitionLowerBound: 'partition_lower_bound',
  partitionUpperBound: 'partition_upper_bound',
  batchSize: 'batch_size',
  batchIntervalMs: 'batch_interval_ms',
  maxRetries: 'max_retries',
  isExactlyOnce: 'is_exactly_once',
  xaDataSourceClassName: 'xa_data_source_class_name',
  maxCommitAttempts: 'max_commit_attempts',
  transactionTimeoutSec: 'transaction_timeout_sec'
}

export const BaseFileParams = {
  path: 'path',
  type: 'type',
  schema: 'schema',
  delimiter: 'delimiter',
  parsePartitionFromPath: 'parse_partition_from_path',
  dateFormat: 'date_format',
  timeFormat: 'time_format',
  datetimeFormat: 'datetime_format',
  fileNameExpression: 'file_name_expression',
  fileFormat: 'file_format',
  filenameTimeFormat: 'filename_time_format',
  fieldDelimiter: 'field_delimiter',
  rowDelimiter: 'row_delimiter',
  partitionBy: 'partition_by',
  partitionDirExpression: 'partition_dir_expression',
  isPartitionFieldWriteInFile: 'is_partition_field_write_in_file',
  sinkColumns: 'sink_columns',
  isEnableTransaction: 'is_enable_transaction'
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

export const S3FileParams = {
  bucket: 'bucket',
  accessKey: 'access_key',
  accessSecret: 'access_secret',
};

export const HttpParams = {
  method: 'method',
  url: 'url',
  headerArray: 'headerArray',
  header: 'header',
  headerValue: 'headerValue',
  paramArray: 'paramArray',
  param: 'param',
  paramValue: 'paramValue',
  body: 'body',
  format: 'format',
  schema: 'schema',
  pollIntervalMs: 'poll_interval_ms',
  retry: 'retry',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  retryBackoffMaxMs: 'retry_backoff_max_ms',
};

export const WeChatParams = {
  url: 'url',
  mentionedArray: 'mentionedArray',
  mentionedList: 'mentioned_list',
  userId: 'userId',
  mentionedMobileArray: 'mentionedMobileArray',
  mentionedMobileList: 'mentioned_mobile_list',
  mobile: 'mobile',
};

export const FeishuParams = {
  url: 'url',
  headerArray: 'headerArray',
  headers: 'headers',
  header: 'header',
  headerValue: 'headerValue'
};

export const DingTalkParams = {
  url: 'url',
  secret: 'secret',
};

export const EmailParams = {
  emailHost: 'email_host',
  emailTransportProtocol: 'email_transport_protocol',
  emailFromAddress: 'email_from_address',
  emailSmtpAuth: 'email_smtp_auth',
  emailAuthorizationCode: 'email_authorization_code',
  emailToAddress: 'email_to_address',
  emailMessageHeadline: 'email_message_headline',
  emailMessageContent: 'email_message_content',
};

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
};

export const ClickHouseParams = {
  dataSourceType: 'ClickHouse',
  splitMode: 'split_mode',
  shardingKey: 'sharding_key',
  clickhouseConf: 'clickhouse_conf',
  key: 'key',
  value: 'value',
  sql: 'sql',
  database: 'database',
};

export const HiveParams = {
  tableName: 'table_name',
  metastoreUri: 'metastore_uri',
  partitionBy: 'partition_by',
  sinkColumns: 'sink_columns',
  isEnableTransaction: 'is_enable_transaction',
  saveMode: 'save_mode',
};

export const KuduParams = {
  kuduMaster: 'kudu_master',
  kuduTable: 'kudu_table',
  columnsList: 'columnsList',
  saveMode: 'save_mode',
};

export const KafkaParams = {
  topic: 'topic',
  pattern: 'pattern',
  kafkaConf: 'kafkaConf',
  key: 'key',
  value: 'value',
  consumerGroup: 'consumer.group',
  commit_on_checkpoint: 'commit_on_checkpoint',
  format: 'format',
  schema: 'schema',
  fieldDelimiter: 'field_delimiter',
  semantic: 'semantic',
  partitionKey: 'partition_key',
  partition: 'partition',
  assignPartitions: 'assign_partitions',
  assignPartitionArray: 'assignPartitionArray',
  assignPartition: 'assignPartition',
  transactionPrefix: 'transaction_prefix',
};

export const IoTDBParams = {
  nodeUrls: 'node_urls',
  username: 'username',
  password: 'password',
  sql: 'sql',
  fields: 'fields',
  fieldArray: 'fieldArray',
  fetchSize: 'fetch_size',
  thriftDefaultBufferSize: 'thrift_default_buffer_size',
  thriftMaxFrameSize: 'thrift_max_frame_size',
  enableCacheLeader: 'enable_cache_leader',
  version: 'version',
  numPartitions: 'num_partitions',
  lowerBound: 'lower_bound',
  upperBound: 'upper_bound',
  batchSize: 'batch_size',
  batchIntervalMs: 'batch_interval_ms',
  maxRetries: 'max_retries',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  maxRetryBackoffMs: 'max_retry_backoff_ms',
  defaultThriftBufferSize: 'default_thrift_buffer_size',
  maxThriftFrameSize: 'max_thrift_frame_size',
  zoneId: 'zone_id',
  enableRpcCompression: 'enable_rpc_compression',
  connectionTimeoutInMs: 'connection_timeout_in_ms',
};

export const MondoDBParams = {
  uri: 'uri',
  database: 'database',
  collection: 'collection'
};

export const RedisParams = {
  host: 'host',
  port: 'port',
  auth: 'auth',
  keys: 'keys',
  dataType: 'data_type',
  format: 'format',
  schema: 'schema',
  key: 'key'
};

export const PulsarParams = {
  clientServiceUrl: 'client.service-url',
  adminServiceUrl: 'admin.service-url',
  authPluginClass: 'auth.plugin-class',
  authParams: 'auth.params',
  subscriptionName: 'subscription.name',
  topic: 'topic',
  topicPattern: 'topicPattern',
  topicDiscoveryInterval: 'topic-discovery.interval',
  pollTimeout: 'poll.timeout',
  pollInterval: 'poll.interval',
  pollBatchSize: 'poll.batch.size',
  cursorStartupMode: 'cursor.startup.mode',
  cursorStartupTimestamp: 'cursor.startup.timestamp',
  cursorResetMode: 'cursor.reset.mode',
  cursorStopMode: 'cursor.stop.mode',
  cursorStopTimestamp: 'cursor.stop.timestamp'
};

export const DatahubParams = {
  endpoint: 'endpoint',
  accessId: 'accessId',
  accessKey: 'accessKey',
  project: 'project',
  topic: 'topic',
  timeout: 'timeout',
  retryTimes: 'retryTimes'
};

export const ElasticsearchParams = {
  hosts: 'hosts',
  username: 'username',
  password: 'password',
  index: 'index',
  maxRetrySize: 'max_retry_size',
  maxBatchSize: 'max_batch_size'
};

export const Neo4jParams = {
  uri: 'uri',
  username: 'username',
  password: 'password',
  bearerToken: 'bearer_token',
  kerberosTicket: 'kerberos_ticket',
  database: 'database',
  query: 'query',
  queryParamPosition: 'queryParamPosition',
  maxTransactionRetryTime: 'max_transaction_retry_time',
  maxConnectionTimeout: 'max_connection_timeout',
  queryParamPositionArray: 'queryParamPositionArray',
  field: 'field',
  position: 'position',
};

export const SentryParams = {
  dsn: 'dsn',
  env: 'env',
  release: 'release',
  enableExternalConfiguration: 'enableExternalConfiguration',
  cacheDirPath: 'cacheDirPath',
  maxCacheItems: 'maxCacheItems',
  flushTimeoutMillis: 'flushTimeoutMillis',
  maxQueueSize: 'maxQueueSize'
};

export const InfluxDBParams = {
  url: 'url',
  username: 'username',
  password: 'password',
  database: 'database',
  sql: 'sql',
  fields: 'fields',
  fieldArray: 'fieldArray',
  splitColumn: 'split_column',
  lowerBound: 'lower_bound',
  upperBound: 'upper_bound',
  partitionNum: 'partition_num',
  epoch: 'epoch',
  queryTimeoutSec: 'query_timeout_sec',
  connectTimeoutMs: 'connect_timeout_ms'
};
