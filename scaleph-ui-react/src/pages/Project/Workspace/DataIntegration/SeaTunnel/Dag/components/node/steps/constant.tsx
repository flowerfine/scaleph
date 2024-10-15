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
  xmlRootTag: 'xml_root_tag',
  xmlRowTag: 'xml_row_tag',
  xmlUseAttrFormat: 'xml_use_attr_format',
}

export const ColumnParams = {
  readColumns: 'read_columns',
  readColumnArray: 'readColumnArray',
  readColumn: 'readColumn'
}

export const CommonListParams = {
  commonList: '_common_list_',
  commonListItem: '_common_config_item_',
}

export const CommonConfigParams = {
  commonConfig: '_common_config_',
  commonConfigKey: '_common_config_key_',
  commonConfigValue: '_common_config_value_'
}

export const FakeParams = {
  tablesConfigs: 'tables_configs',
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

export const ConsoleParams = {
  logPrintData: 'log.print.data',
  logPrintDataDelayMs: 'log.print.delay.ms'
}

export const JdbcParams = {
  connectionCheckTimeoutSec: 'connection_check_timeout_sec',
  compatibleMode: 'compatible_mode',
  database: 'database',
  table: 'table',
  supportUpsert: 'support_upsert_by_query_primary_key_exist',
  generateSinkSql: 'generate_sink_sql',
  primaryKeys: 'primary_keys',
  enableUpsert: 'enable_upsert',
  query: 'query',
  tablePath: 'table_path',
  tableList: 'table_list',
  whereCondition: 'where_condition',
  partitionColumn: 'partition_column',
  partitionLowerBound: 'partition_lower_bound',
  partitionUpperBound: 'partition_upper_bound',
  partitionNum: 'partition_num',
  splitSize: 'split.size',
  splitEvenDistributionFactorLowerBound: 'split.even-distribution.factor.lower-bound',
  splitEvenDistributionFactorUpperBound: 'split.even-distribution.factor.upper-bound',
  splitSampleShardingThreshold: 'split.sample-sharding.threshold',
  splitInverseSamplingRate: 'split.inverse-sampling.rate',
  fetchSize: 'fetch_size',
  batchSize: 'batch_size',
  maxRetries: 'max_retries',
  isExactlyOnce: 'is_exactly_once',
  xaDataSourceClassName: 'xa_data_source_class_name',
  maxCommitAttempts: 'max_commit_attempts',
  transactionTimeoutSec: 'transaction_timeout_sec',
  autoCommit: 'auto_commit',
  fieldIde: 'field_ide',
  schemaSaveMode: 'schema_save_mode',
  dataSaveMode: 'data_save_mode',
  customSql: 'custom_sql',
  useCopyStatement: 'use_copy_statement',
  properties: 'properties',
}

export const BaseFileParams = {
  path: 'path',
  tmpPath: 'tmp_path',
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
  encoding: 'encoding',
  maxRowsInMemory: 'max_rows_in_memory',
  sheetName: 'sheet_name',
  enableHeaderWrite: 'enable_header_write',
};

export const HDFSFileParams = {
  remoteUser: 'remote_user',
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
  connectTimeoutMs: 'connect_timeout_ms',
  socketTimeoutMs: 'socket_timeout_ms',
  headers: 'headers',
  params: 'params',
  body: 'body',
  pageing: 'pageing',
  pagingPageField: 'pageing.page_field',
  pagingTotalPageSize: 'pageing.total_page_size',
  pagingBatchSize: 'pageing.batch_size',
  pageField: 'page_field',
  totalPageSize: 'total_page_size',
  batchSize: 'batch_size',
  enableMultiLines: 'enable_multi_lines',
  contentField: 'content_field',
  format: 'format',
  schema: 'schema',
  jsonField: 'json_field',
  pollIntervalMs: 'poll_interval_ms',
  retry: 'retry',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  retryBackoffMaxMs: 'retry_backoff_max_ms',
};

export const WeChatParams = {
  url: 'url',
  mentionedList: 'mentioned_list',
  mentionedMobileList: 'mentioned_mobile_list',
};

export const FeishuParams = {
  url: 'url',
  headers: 'headers',
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
  emailSmtpPort: 'email_smtp_port',
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
  catalogName: 'catalog_name',
  namespace: 'namespace',
  table: 'table',
  catalogConfig: 'iceberg.catalog.config',
  catalogConfigType: 'type',
  catalogConfigUri: 'uri',
  catalogConfigWarehouse: 'warehouse',
  hadoopConfig: 'hadoop.config',
  hadoopConfigKey: 'hadoopConfigKey',
  hadoopConfigValue: 'hadoopConfigValue',
  icebergHadoopConfPath: 'iceberg.hadoop-conf-path',
  icebergTableWriteProps: 'iceberg.table.write-props',
  icebergTableAutoCreateProps: 'iceberg.table.auto-create-props',
  caseSensitive: 'case_sensitive',
  icebergTableSchemaEvolutionEnabled: 'iceberg.table.schema-evolution-enabled',
  icebergTableUpsertModeEnabled: 'iceberg.table.upsert-mode-enabled',
  icebergTablePrimaryKeys: 'iceberg.table.primary-keys',
  icebergTablePartitionKeys: 'iceberg.table.partition-keys',
  icebergTableCommitBranch: 'iceberg.table.commit-branch',

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
  abortDropPartitionMetadata: 'abort_drop_partition_metadata',
};

export const KuduParams = {
  kuduMaster: 'kudu_master',
  enableKerberos: 'enable_kerberos',
  kerberosPrincipal: 'kerberos_principal',
  kerberosKeytab: 'kerberos_keytab',
  kerberosKrb5conf: 'kerberos_krb5conf',
  clientWorkerCount: 'client_worker_count',
  clientDefaultOperationTimeoutMs: 'client_default_operation_timeout_ms',
  clientDefaultAdminOperationTimeoutMs: 'client_default_admin_operation_timeout_ms',
  tableName: 'table_name',
  saveMode: 'save_mode',
  sessionFlushMode: 'session_flush_mode',
  batchSize: 'batch_size',
  bufferFlushInterval: 'buffer_flush_interval',
  ignoreNotFound: 'ignore_not_found',
  ignoreNotDuplicate: 'ignore_not_duplicate',
  scanTokenQueryTimeout: 'scan_token_query_timeout',
  scanTokenBatchSizeBytes: 'scan_token_batch_size_bytes',
  filter: 'filter',
  schema: 'schema',
  tableList: 'table_list',
};

export const KafkaParams = {
  topic: 'topic',
  pattern: 'pattern',
  partitionDiscoveryIntervalMillis: 'partition-discovery.interval-millis',
  kafkaConfig: 'kafka.config',
  consumerGroup: 'consumer.group',
  commit_on_checkpoint: 'commit_on_checkpoint',
  format: 'format',
  formatErrorHandleWay: 'format_error_handle_way',
  schema: 'schema',
  fieldDelimiter: 'field_delimiter',
  semantic: 'semantic',
  partitionKeyFields: 'partition_key_fields',
  partition: 'partition',
  assignPartitions: 'assign_partitions',
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
  maxRetries: 'max_retries',
  retryBackoffMultiplierMs: 'retry_backoff_multiplier_ms',
  maxRetryBackoffMs: 'max_retry_backoff_ms',
  defaultThriftBufferSize: 'default_thrift_buffer_size',
  maxThriftFrameSize: 'max_thrift_frame_size',
  thriftMaxFrameSize: 'thrift_max_frame_size',
  zoneId: 'zone_id',
  enableRpcCompression: 'enable_rpc_compression',
  connectionTimeoutInMs: 'connection_timeout_in_ms',
  keyDevice: 'key_device',
  keyTimestamp: 'key_timestamp',
  keyMeasurementFields: 'key_measurement_fields',
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
  dbNum: 'db_num',
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
  fieldDelimiter: 'field_delimiter',
  semantics: 'semantics',
  transactionTimeout: 'transaction_timeout',
  messageRoutingMode: 'message.routing.mode',
  partitionKeyFields: 'partition_key_fields',
  pulsarConfig: 'pulsar.config',
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
  keyDelimiter: 'key_delimiter',
  maxRetryCount: 'max_retry_count',
  maxBatchSize: 'max_batch_size',
  query: "query",
  scrollTime: "scroll_time",
  scrollSize: "scroll_size",
  source: "source",
  arrayColumn: "array_column",
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
  writeMode: 'write_mode',
  maxBatchSize: 'max_batch_size',
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
  batchSize: 'batch_size',
  batchType: 'batch_type',
  asyncWrite: 'async_write'
};

export const DorisParams = {
  database: 'database',
  table: 'table',
  sinkLabelPrefix: 'sink.label-prefix',
  sinkEnable2PC: 'sink.enable-2pc',
  sinkEnableDelete: 'sink.enable-delete',
  sinkCheckInterval: 'sink.check-interval',
  sinkMaxRetries: 'sink.max-retries',
  sinkBufferSize: 'sink.buffer-size',
  sinkBufferCount: 'sink.buffer-count',
  dorisBatchSize: 'doris.batch.size',
  needsUnsupportedTypeCasting: 'needs_unsupported_type_casting',
  schemaSaveMode: 'schema_save_mode',
  dataSaveMode: 'data_save_mode',
  saveModeCreateTemplate: 'save_mode_create_template',
  customSql: 'custom_sql',
  dorisConfig: 'doris.config',
  dorisConfigArray: 'dorisConfigArray',
  dorisConfigProperty: 'property',
  dorisConfigValue: 'value',

  dorisReadField: 'doris.read.field',
  dorisFiterQuery: 'doris.filter.query',
  dorisRequestQueryTimeoutS: 'doris.request.query.timeout.s',
  dorisRequestConnectTimeoutMs: 'doris.request.connect.timeout.ms',
  dorisRequestReadTimeoutMs: 'doris.request.read.timeout.ms',
  dorisRequestRetries: 'doris.request.retries',
  dorisExecMemLimit: 'doris.exec.mem.limit',
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
  customSql: 'custom_sql',
  starrocksConfig: 'starrocks.config',
  scanFilter: 'scan_filter',
  scanConnectTimeoutMs: 'scan_connect_timeout_ms',
  scanQueryTimeoutSec: 'scan_query_timeout_sec',
  scanKeepAliveMin: 'scan_keep_alive_min',
  scanBatchRows: 'scan_batch_rows',
  scanMemLimit: 'scan_mem_limit',
  requestTabletSize: 'request_tablet_size',
  httpSocketTimeoutMs: 'http_socket_timeout_ms',
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
  batchIntervalMs: 'batch_interval_ms',
  scanItemLimit: 'scan_item_limit',
  parallelScanThreads: 'parallel_scan_threads'
};

export const S3RedshiftParams = {
  jdbcUrl: 'jdbc_url',
  jdbcUser: 'jdbc_user',
  jdbcPassword: 'jdbc_password',
  executeSql: 'execute_sql',
  bucket: 'bucket'
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
  schemas: 'schema-names',
  tables: 'table-names',
  tableConfig: 'table-names-config',
  tableConfigTableKey: 'table',
  tableConfigPrimaryKey: 'primaryKeys',
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
  slotName: 'slot.name',
  decodingPluginName: 'decoding.plugin.name',
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

export const OracleCDCParams = {
  schemaNames: 'schema-names',
  useSelectCount: 'use_select_count',
  skipAnalyze: 'skip_analyze',
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
  rowkeyDelimiter: 'rowkey_delimiter',
  versionColumn: 'version_column',
  nullMode: 'null_mode',
  walWrite: 'wal_write',
  writeBufferSize: 'write_buffer_size',
  encoding: 'encoding',
  hbaseExtraConfig: 'hbase_extra_config',
}

export const PaimonParams = {
  warehouse: 'warehouse',
  database: 'database',
  table: 'table',
  hdfsSitePath: 'hdfs_site_path',
  paimonHadoopConfPath: 'paimon.hadoop.conf-path',
  paimonHadoopConf: 'paimon.hadoop.conf',
  paimonTablePrimaryKeys: 'paimon.table.primary-keys',
  paimonTablePartitionKeys: 'paimon.table.partition-keys',
  paimonTableWriteProps: 'paimon.table.write-props',
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
