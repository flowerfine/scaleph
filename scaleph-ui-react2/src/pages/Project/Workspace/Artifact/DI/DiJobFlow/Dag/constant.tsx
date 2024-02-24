import {IGraphCommand, XFlowGraphCommands, XFlowNodeCommands} from '@antv/xflow';

export const DND_RENDER_ID = 'DND_NODE';
export const GROUP_NODE_RENDER_ID = 'GROUP_NODE_RENDER_ID';
export const EDGE_NODE_RENDER_ID = 'EDGE_NODE_RENDER_ID';
export const NODE_WIDTH = 180;
export const NODE_HEIGHT = 32;
export const ZOOM_OPTIONS = {maxScale: 2, minScale: 0.5};
export const CONNECTION_PORT_TYPE = {source: 'outPort', target: 'inPort'};

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

  export const GRAPH_HELP: IGraphCommand = {
    id: 'xflow:graph-help',
    label: 'graph-help',
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
  fieldArray: 'fieldArray',
  field: 'field',
  type: 'type',
  delimiter: 'delimiter',
  skipHeaderRowNumber: 'skip_header_row_number',
  sheetName: 'sheet_name',
}

export const ColumnParams = {
  readColumns: 'read_columns',
  readColumnArray: 'readColumnArray',
  readColumn: 'readColumn'
}

export const FakeParams = {
  rows: 'rows',
  rowNum: 'row.num',
  splitNum: 'split.num',
  splitReadInterval: 'split.read-interval',
  mapSize: 'map.size',
  arraySize: 'array.size',
  bytesLength: 'bytes.length',
  stringLength: 'string.length',

  stringFakeMode: 'string.fake.mode',
  stringTemplate: 'string.template',

  tinyintFakeMode: 'tinyint.fake.mode',
  tinyintMin: 'tinyint.min',
  tinyintMax: 'tinyint.max',
  tinyintTemplate: 'tinyint.template',

  smallintFakeMode: 'smallint.fake.mode',
  smallintMin: 'smallint.min',
  smallintMax: 'smallint.max',
  smallintTemplate: 'smallint.template',

  intFakeMode: 'int.fake.mode',
  intMin: 'int.min',
  intMax: 'int.max',
  intTemplate: 'int.template',

  bigintFakeMode: 'bigint.fake.mode',
  bigintMin: 'bigint.min',
  bigintMax: 'bigint.max',
  bigintTemplate: 'bigint.template',

  floatFakeMode: 'float.fake.mode',
  floatMin: 'float.min',
  floatMax: 'float.max',
  floatTemplate: 'float.template',

  doubleFakeMode: 'double.fake.mode',
  doubleMin: 'double.min',
  doubleMax: 'double.max',
  doubleTemplate: 'double.template',

  dateYearTemplate: 'date.year.template',
  dateMonthTemplate: 'date.month.template',
  dateDayTemplate: 'date.day.template',
  timeHourTemplate: 'time.hour.template',
  timeMinuteTemplate: 'time.minute.template',
  timeSecondTemplate: 'time.second.template'
}

export const JdbcParams = {
  connectionCheckTimeoutSec: 'connection_check_timeout_sec',
  compatibleMode: 'compatible_mode',
  database: 'database',
  table: 'table',
  supportUpsert: 'support_upsert_by_query_primary_key_exist',
  generateSinkSql: 'generate_sink_sql',
  primaryKeys: 'primary_keys',
  primaryKeyArray: 'primaryKeyArray',
  primaryKey: 'key',
  query: 'query',
  partitionColumn: 'partition_column',
  partitionLowerBound: 'partition_lower_bound',
  partitionUpperBound: 'partition_upper_bound',
  partitionNum: 'partition_num',
  fetchSize: 'fetch_size',
  batchSize: 'batch_size',
  maxRetries: 'max_retries',
  isExactlyOnce: 'is_exactly_once',
  xaDataSourceClassName: 'xa_data_source_class_name',
  maxCommitAttempts: 'max_commit_attempts',
  transactionTimeoutSec: 'transaction_timeout_sec',
  autoCommit: 'auto_commit'
}

export const BaseFileParams = {
  path: 'path',
  fileFilterPattern: 'file_filter_pattern',
  fileFormatType: 'file_format_type',
  readColumns: 'read_columns',
  schema: 'schema',
  delimiter: 'delimiter',
  parsePartitionFromPath: 'parse_partition_from_path',
  dateFormat: 'date_format',
  timeFormat: 'time_format',
  datetimeFormat: 'datetime_format',
  customFilename: 'custom_filename',
  fileNameExpression: 'file_name_expression',
  filenameTimeFormat: 'filename_time_format',
  fieldDelimiter: 'field_delimiter',
  rowDelimiter: 'row_delimiter',
  havePartition: 'have_partition',
  partitionBy: 'partition_by',
  partitionDirExpression: 'partition_dir_expression',
  isPartitionFieldWriteInFile: 'is_partition_field_write_in_file',
  sinkColumns: 'sink_columns',
  isEnableTransaction: 'is_enable_transaction',
  batchSize: 'batch_size',
  compressCodec: 'compress_codec',
  maxRowsInMemory: 'max_rows_in_memory',
  sheetName: 'sheet_name'
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
  hadoopS3Properties: 'hadoopS3Properties',
  key: 'key',
  value: 'value',
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
  enableMultiLines: 'enable_multi_lines',
  schema: 'schema',
  contentField: 'content_field',
  jsonField: 'jsonField',
  key: 'key',
  path: 'path',
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
  primaryKey: 'primary_key',
  supportUpsert: 'support_upsert',
  allowExperimentalLightweightDelete: 'allow_experimental_lightweight_delete',
  clickhouseConf: 'clickhouse.config',
  clickhouseConfArray: 'clickhouseConfArray',
  key: 'key',
  value: 'value',
  sql: 'sql',
  serverTimeZone: 'server_time_zone',
};

export const HiveParams = {
  tableName: 'table_name',
  metastoreUri: 'metastore_uri',
  readPartitions: 'read_partitions',
  readPartitionArray: 'read_partitions',
  readPartition: 'read_partition',
};

export const KuduParams = {
  kuduMaster: 'kudu_master',
  kuduTable: 'kudu_table',
  columnsList: 'columnsList'
};

export const KafkaParams = {
  topic: 'topic',
  pattern: 'pattern',
  partitionDiscoveryIntervalMillis: 'partition-discovery.interval-millis',
  kafkaConfig: 'kafka.config',
  kafkaConf: 'kafkaConf',
  key: 'key',
  value: 'value',
  consumerGroup: 'consumer.group',
  commit_on_checkpoint: 'commit_on_checkpoint',
  format: 'format',
  formatErrorHandleWay: 'format_error_handle_way',
  schema: 'schema',
  fieldDelimiter: 'field_delimiter',
  semantic: 'semantic',
  partitionKeyFields: 'partition_key_fields',
  partitionKeyArray: 'partitionKeyArray',
  partitionKey: 'partitionKey',
  partition: 'partition',
  assignPartitions: 'assign_partitions',
  assignPartitionArray: 'assignPartitionArray',
  assignPartition: 'assignPartition',
  transactionPrefix: 'transaction_prefix',
  startModeTimestamp: 'start_mode.timestamp',
  startModeOffsets: 'start_mode.offsets',
};

export const IoTDBParams = {
  nodeUrls: 'node_urls',
  username: 'username',
  password: 'password',
  sql: 'sql',
  fields: 'fields',
  fetchSize: 'fetch_size',
  thriftDefaultBufferSize: 'thrift_default_buffer_size',
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
  keyDevice: 'key_device',
  keyTimestamp: 'key_timestamp',
  keyMeasurementFields: 'key_measurement_fields',
  keyMeasurementFieldArray: 'keyMeasurementFieldArray',
  keyMeasurementField: 'keyMeasurementField',
  storageGroup: 'storage_group',
};

export const MongoDBParams = {
  uri: 'uri',
  database: 'database',
  collection: 'collection',
  matchQuery: 'match.query',
  matchProjection: 'match.projection',
  partitionSplitKey: 'partition.split-key',
  partitionSplitSize: 'partition.split-size',
  cursorNoTimeout: 'cursor.no-timeout',
  fetchSize: 'fetch.size',
  maxTimeMin: 'max.time-min',
  flatSyncString: 'flat.sync-string',
  bufferFlushMaxRows: 'buffer-flush.max-rows',
  bufferFlushInterval: 'buffer-flush.interval',
  retryMax: 'retry.max',
  retryInterval: 'retry.interval',
  upsertEnable: 'upsert-enable',
  transaction: 'transaction',
  primaryKey: 'primary-key'
};

export const RedisParams = {
  host: 'host',
  port: 'port',
  auth: 'auth',
  keys: 'keys',
  dataType: 'data_type',
  hashKeyParseMode: 'hash_key_parse_mode',
  format: 'format',
  schema: 'schema',
  key: 'key',
  expire: 'expire'
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
  cursorStopTimestamp: 'cursor.stop.timestamp',
  format: 'format',
  fieldDelimiter: 'field_delimiter'
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

export const RocketMQParams = {
  nameSrvAddr: 'name.srv.addr',
  aclEnabled: 'acl.enabled',
  aclEnabledField: 'acl_enabled',
  accessKey: 'access.key',
  secretKey: 'secret.key',
  format: 'format',
  fieldDelimiter: 'field.delimiter',

  topics: 'topics',
  partitionDiscoveryIntervalMillis: 'partition.discovery.interval.millis',
  consumerGroup: 'consumer.group',
  commitOnCheckpoint: 'commit.on.checkpoint',
  schema: 'schema',
  startMode: 'start.mode',
  startModeField: 'startModeField',
  startModeTimestamp: 'start.mode.timestamp',
  startModeOffsets: 'start.mode.offsets',
  startModeOffsetsList: 'startModeOffsetsList',
  specificPartition: 'specificPartition',
  specificPartitionOffset: 'specificPartitionOffset',
  batchSize: 'batch.size',
  consumerPollTimeoutMillis: 'consumer.poll.timeout.millis',

  topic: 'topic',
  semantic: 'semantic',
  producerGroup: 'producer.group',
  partitionKeyFields: 'partition.key.fields',
  exactlyOnce: 'exactly.once',
  producerSendSync: 'producer.send.sync',
  maxMessageSize: 'max.message.size',
  sendMessageTimeout: 'send.message.timeout'
};

export const ElasticsearchParams = {
  hosts: 'hosts',
  username: 'username',
  password: 'password',
  index: 'index',
  primaryKeys: 'primary_keys',
  primaryKeyArray: 'primaryKeyArray',
  primaryKey: 'primaryKey',
  keyDelimiter: 'key_delimiter',
  maxRetryCount: 'max_retry_count',
  maxBatchSize: 'max_batch_size',
  query: "query",
  scrollTime: "scroll_time",
  scrollSize: "scroll_size",
  source: "source",
  sourceArray: "sourceArray",
  sourceField: "sourceField",
  schema: "schema"
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
  schema: 'schema',
  fieldArray: 'fieldArray',
  splitColumn: 'split_column',
  lowerBound: 'lower_bound',
  upperBound: 'upper_bound',
  partitionNum: 'partition_num',
  epoch: 'epoch',
  queryTimeoutSec: 'query_timeout_sec',
  connectTimeoutMs: 'connect_timeout_ms',
  measurement: 'measurement',
  keyKime: 'key_time',
  keyTagArray: 'keyTagArray',
  keyTags: 'key_tags',
  batchSize: 'batch_size',
  batchIntervalMs: 'batch_interval_ms',
  maxRetries: 'max_retries',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms'
};

export const CassandraParams = {
  consistencyLevel: 'consistency_level',
  cql: 'cql',
  table: 'table',
  fields: 'fields',
  fieldArray: 'fieldArray',
  field: 'field',
  batchSize: 'batch_size',
  batchType: 'batch_type',
  asyncWrite: 'async_write'
};

export const DorisParams = {
  database: 'database',
  tableIdentifier: 'table.identifier',
  sinkLabelPrefix: 'sink.label-prefix',
  sinkEnable2PC: 'sink.enable-2pc',
  sinkEnableDelete: 'sink.enable-delete',
  sinkCheckInterval: 'sink.check-interval',
  sinkMaxRetries: 'sink.max-retries',
  sinkBufferSize: 'sink.buffer-size',
  sinkBufferCount: 'sink.buffer-count',
  dorisConfig: 'doris.config',
  dorisConfigArray: 'dorisConfigArray',
  dorisConfigProperty: 'property',
  dorisConfigValue: 'value',
};

export const StarRocksParams = {
  baseUrl: 'base-url',
  database: 'database',
  table: 'table',
  labelPrefix: 'labelPrefix',
  batchMaxRows: 'batch_max_rows',
  batchMaxBytes: 'batch_max_bytes',
  batchIntervalMs: 'batch_interval_ms',
  maxRetries: 'max_retries',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  maxRetryBackoffMs: 'max_retry_backoff_ms',
  enableUpsertDelete: 'enable_upsert_delete',
  saveModeCreateTemplate: 'save_mode_create_template',
  starrocksConfig: 'starrocks.config',
  starrocksConfigMap: 'starrocksConfigMap',
  starrocksConfigKey: 'starrocksConfigKey',
  starrocksConfigValue: 'starrocksConfigValue',
  scanFilter: 'scan_filter',
  scanConnectTimeoutMs: 'scan_connect_timeout_ms',
  scanQueryTimeoutSec: 'scan_query_timeout_sec',
  scanKeepAliveMin: 'scan_keep_alive_min',
  scanBatchRows: 'scan_batch_rows',
  scanMemLimit: 'scan_mem_limit',
  requestTabletSize: 'request_tablet_size',
};

export const MaxComputeParams = {
  project: 'project',
  tableName: 'table_name',
  partitionSpec: 'partition_spec',
  splitRow: 'split_row',
  overwrite: 'overwrite'
};

export const AmazonDynamoDBParams = {
  url: 'url',
  region: 'region',
  accessKeyId: 'access_key_id',
  secretAccessKey: 'secret_access_key',
  table: 'table',
  schema: 'schema',
  batchSize: 'batch_size',
  batchIntervalMs: 'batch_interval_ms'
};

export const S3RedshiftParams = {
  jdbcUrl: 'jdbc_url',
  jdbcUser: 'jdbc_user',
  jdbcPassword: 'jdbc_password',
  executeSql: 'execute_sql'
};

export const OpenMLDBParams = {
  clusterMode: 'cluster_mode',
  host: 'host',
  port: 'port',
  zkHost: 'zk_host',
  zkPath: 'zk_path',
  database: 'database',
  sql: 'sql',
  sessionTimeout: 'session_timeout',
  requestTimeout: 'request_timeout',
};

export const CDCParams = {
  baseUrl: 'base-url',
  username: 'username',
  password: 'password',
  databases: 'database-names',
  tables: 'table-names',
  startupMode: 'startup.mode',
  startupTimestamp: 'startup.timestamp',
  startupSpecificOffsetFile: 'startup.specific-offset.file',
  startupSpecificOffsetPos: 'startup.specific-offset.pos',
  stopMode: 'stop.mode',
  stopTimestamp: 'stop.timestamp',
  stopSpecificOffsetFile: 'stop.specific-offset.file',
  stopSpecificOffsetPos: 'stop.specific-offset.pos',
  incrementalParallelism: 'incremental.parallelism',
  snapshotSplitSize: 'snapshot.split.size',
  snapshotFetchSize: 'snapshot.fetch.size',
  serverId: 'server-id',
  serverTimeZone: 'server-time-zone',
  connectTimeout: 'connect.timeout',
  connectMaxRetries: 'connect.max-retries',
  connectionPoolSize: 'connection.pool.size',
  chunkKeyEvenDistributionFactorUpperBound: 'chunk-key.even-distribution.factor.upper-bound',
  chunkKeyEvenDistributionFactorLowerBound: 'chunk-key.even-distribution.factor.lower-bound',
  sampleShardingThreshold: 'sample-sharding.threshold',
  inverseSamplingRate: 'inverse-sampling.rate',
  exactlyOnce: 'exactly_once',
  debeziums: 'debezium',
  debeziumProperties: 'debeziumProperties',
  debeziumProperty: 'debeziumProperty',
  debeziumValue: 'debeziumValue',
  format: 'format',
};

export const MongoDBCDCParams = {
  hosts: 'hosts',
  username: 'username',
  password: 'password',
  database: 'database',
  collection: 'collection',
  connectionOptions: 'connection.options',
  batchSize: 'batch.size',
  pollMaxBatchSize: 'poll.max.batch.size',
  pollAwaitTimeMs: 'poll.await.time.ms',
  heartbeatIntervalMs: 'heartbeat.interval.ms',
  incrementalSnapshotChunkSizeMb: 'incremental.snapshot.chunk.size.mb'
};

export const HbaseParams = {
  zookeeperQuorum: 'zookeeper_quorum',
  table: 'table',
  familyName: 'family_name',
  rowkeyColumn: 'rowkey_column',
  rowkeyColumnArray: 'rowkeyColumnArray',
  rowkeyColumnValue: 'rowkeyColumnArray',
  rowkeyDelimiter: 'rowkey_delimiter',
  versionColumn: 'version_column',
  nullMode: 'null_mode',
  walWrite: 'wal_write',
  writeBufferSize: 'write_buffer_size',
  encoding: 'encoding',
  hbaseExtraConfig: 'hbase_extra_config',
  hbaseExtraConfigMap: 'hbaseExtraConfigMap',
  hbaseExtraConfigKey: 'hbaseExtraConfigKey',
  hbaseExtraConfigValue: 'hbaseExtraConfigValue'
}

export const PaimonParams = {
  warehouse: 'warehouse',
  database: 'database',
  table: 'table',
  hdfsSitePath: 'hdfs_site_path'
}

export const CopyParams = {
  srcField: 'src_field',
  destField: 'dest_field'
}

export const FieldMapperParams = {
  fieldMapper: 'field_mapper',
  fieldMapperGroup: 'fieldMapperGroup',
  srcField: 'src_field',
  destField: 'dest_field'
}

export const FilterRowKindParams = {
  includeKinds: 'include_kinds',
  excludeKinds: 'exclude_kinds'
}

export const FilterParams = {
  fields: 'fields',
  fieldArray: 'fieldArray',
  field: 'field',
}

export const ReplaceParams = {
  replaceField: 'replace_field',
  pattern: 'pattern',
  replacement: 'replacement',
  isRegex: 'is_regex',
  replaceFirst: 'replace_first'
}

export const SplitParams = {
  separator: 'separator',
  splitField: 'split_field',
  outputFields: 'output_fields',
  outputFieldArray: 'outputFieldArray',
  outputField: 'outputField'
}


export const SqlParams = {
  query: 'query'
}
