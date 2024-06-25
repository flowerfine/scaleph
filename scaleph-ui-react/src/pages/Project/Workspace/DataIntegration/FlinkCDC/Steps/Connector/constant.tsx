export const MySQLParams = {
  tables: 'tables',
  tablesExclude: 'tables.exclude',
  schemaChangeEnabled: 'schema-change.enabled',
  serverId: 'server-id',
  scanIncrementalSnapshotChunkSize: 'scan.incremental.snapshot.chunk.size',
  scanSnapshotFetchSize: 'scan.snapshot.fetch.size',
  scanIncrementalCloseIdleReaderEnabled: 'scan.incremental.close-idle-reader.enabled',
  scanStartupMode: 'scan.startup.mode',
  startupMode: 'scanStartupMode',
  scanStartupSpecificOffsetFile: 'scan.startup.specific-offset.file',
  scanStartupSpecificOffsetPos: 'scan.startup.specific-offset.pos',
  scanStartupSpecificOffsetGtidSet: 'scan.startup.specific-offset.gtid-set',
  scanStartupSpecificOffsetSkipEvents: 'scan.startup.specific-offset.skip-events',
  scanStartupSpecificOffsetSkipRows: 'scan.startup.specific-offset.skip-rows',
  connectTimeout: 'connect.timeout',
  connectMaxRetries: 'connect.max-retries',
  connectionPoolSize: 'connection.pool.size',
  heartbeatInterval: 'heartbeat.interval',
  jdbcProperties: 'jdbc.properties',
  debezium: 'debezium',
}

export const KafkaParams = {
  propertiesBootstrapServers: 'properties.bootstrap.servers',
  topic: 'topic',
  valueFormat: 'value.format',
  sinkAddTableIdToHeaderEnabled: 'sink.add-tableId-to-header-enabled',
  sinkCustomHeader: 'sink.custom-header',
  properties: 'properties'
}

export const DorisParams = {
  autoRedirect: 'auto-redirect',
  sinkEnableBatchMode: 'sink.enable.batch-mode',
  sinkEnableBatchModeParam: 'sinkEnableBatchModeParam',
  sinkFlushQueueSize: 'sink.flush.queue-size',
  sinkBufferFlushMaxRows: 'sink.buffer-flush.max-rows',
  sinkBufferFlushMaxBytes: 'sink.buffer-flush.max-bytes',
  sinkBufferFlushInterval: 'sink.buffer-flush.interval',
  sinkProperties: 'sink.properties',
  tableCreateProperties: 'table.create.properties',
}

export const StarRocksParams = {
  sinkLabelPrefix: 'sink.label-prefix',
  tableSchemaChangeTimeout: 'table.schema-change.timeout',
  sinkConnectTimeoutMs: 'sink.connect.timeout-ms',
  sinkWaitForContinueTimeoutMs: 'sink.wait-for-continue.timeout-ms',
  sinkBufferFlushMaxBytes: 'sink.buffer-flush.max-bytes',
  sinkBufferFlushIntervalMs: 'sink.buffer-flush.interval-ms',
  sinkScanFrequencyMs: 'sink.scan-frequency.ms',
  sinkIoThreadCount: 'sink.io.thread-count',
  sinkAtLeastOnceUseTransactionStreamLoad: 'sink.at-least-once.use-transaction-stream-load',
  tableCreateNumBuckets: 'table.create.num-buckets',
  sinkProperties: 'sink.properties',
  tableCreateProperties: 'table.create.properties',
}

