import {
  ColumnParams,
  CommonConfigParams,
  CommonListParams,
  FieldMapperParams,
  FilterParams,
  HttpParams,
  IcebergParams,
  InfluxDBParams,
  IoTDBParams,
  JdbcParams,
  KafkaParams,
  PulsarParams,
  RocketMQParams,
  SchemaParams,
  SplitParams,
  StarRocksParams
} from "./constant";

export const StepSchemaService = {

  formatSchema: (values: Record<string, any>) => {
    const fields: Record<string, any> = {}
    values[SchemaParams.fieldArray]?.forEach(function (item: Record<string, any>) {
      fields[item[SchemaParams.field]] = item[SchemaParams.type];
    });
    const schema: Record<string, any> = {}
    schema[SchemaParams.fields] = fields;
    values[SchemaParams.schema] = JSON.stringify(schema)
    return values
  },

  formatColumns: (values: Record<string, any>) => {
    const columns: Array<any> = []
    values[ColumnParams.readColumnArray]?.forEach(function (item: Record<string, any>) {
      columns.push(item[ColumnParams.readColumn])
    });
    values[ColumnParams.readColumns] = JSON.stringify(columns)
    return values
  },

  formatCommonList: (values: Record<string, any>, param: string, prefix: string) => {
    const list: Array<string> = []
    values[prefix + CommonListParams.commonList]?.forEach(function (item: Record<string, any>) {
      list.push(item[prefix + CommonListParams.commonListItem])
    });
    values[param] = JSON.stringify(list)
    return values
  },

  formatCommonConfig: (values: Record<string, any>, param: string, prefix: string) => {
    const configs: Record<string, any> = {}
    values[prefix + CommonConfigParams.commonConfig]?.forEach(function (item: Record<string, any>) {
      configs[item[prefix + CommonConfigParams.commonConfigKey]] = item[prefix + CommonConfigParams.commonConfigValue];
    });
    values[param] = JSON.stringify(configs)
    return values
  },

  formatUserIds: (values: Record<string, any>) => {
    const userIds: Array<any> = []
    values.mentionedArray?.forEach(function (item: any) {
      userIds.push(item.userId)
    });
    values.mentioned_list = JSON.stringify(userIds)
    return values
  },

  formatMobiles: (values: Record<string, any>) => {
    const mobiles: Array<any> = []
    values.mentionedMobileArray?.forEach(function (item: any) {
      mobiles.push(item.mobile)
    });
    values.mentioned_mobile_list = JSON.stringify(mobiles)
    return values
  },

  formatPositionMapping: (values: Record<string, any>) => {
    const mappings: Record<string, any> = {}
    values.queryParamPositionArray?.forEach(function (item: Record<string, any>) {
      mappings[item.field] = item.position;
    });
    values.queryParamPosition = JSON.stringify(mappings)
    return values
  },

  formatKafkaConf: (values: Record<string, any>) => {
    const config: Record<string, any> = {}
    values[KafkaParams.kafkaConf]?.forEach(function (item: Record<string, any>) {
      config[item[KafkaParams.key]] = item[KafkaParams.value];
    });
    values[KafkaParams.kafkaConfig] = JSON.stringify(config)
    return values
  },

  formatKafkaPartitionKeyFields: (values: Record<string, any>) => {
    const partitionKeyFields: Array<string> = []
    values.partitionKeyArray?.forEach(function (item: Record<string, any>) {
      partitionKeyFields.push(item.partitionKey)
    });
    values[KafkaParams.partitionKeyFields] = JSON.stringify(partitionKeyFields)
    return values
  },

  formatAssginPartitions: (values: Record<string, any>) => {
    const assignPartitions: Array<string> = []
    values.assignPartitionArray?.forEach(function (item: Record<string, any>) {
      assignPartitions.push(item.assignPartition)
    });
    values[KafkaParams.assignPartitions] = JSON.stringify(assignPartitions)
    return values
  },

  formatPulsarConf: (values: Record<string, any>) => {
    const config: Record<string, any> = {}
    values[PulsarParams.pulsarConfigMap]?.forEach(function (item: Record<string, any>) {
      config[item[PulsarParams.pulsarConfigKey]] = item[PulsarParams.pulsarConfigValue];
    });
    values[PulsarParams.pulsarConfig] = JSON.stringify(config)
    return values
  },

  formatPulsarPartitionKeyFields: (values: Record<string, any>) => {
    const partitionKeyFields: Array<string> = []
    values[PulsarParams.partitionKeyFieldArray]?.forEach(function (item: Record<string, any>) {
      partitionKeyFields.push(item[PulsarParams.partitionKeyField])
    });
    values[PulsarParams.partitionKeyFields] = JSON.stringify(partitionKeyFields)
    return values
  },

  formatHadoopS3Properties: (values: Record<string, any>) => {
    const properties: Record<string, any> = {}
    values.hadoopS3Properties?.forEach(function (item: Record<string, any>) {
      properties[item.key] = item.value;
    });
    values.hadoop_s3_properties = JSON.stringify(properties)
    return values
  },

  formatPaging: (values: Record<string, any>) => {
    const paging: Record<string, any> = {}
    paging[HttpParams.pageField] = values[HttpParams.pagingPageField]
    paging[HttpParams.totalPageSize] = values[HttpParams.pagingTotalPageSize]
    paging[HttpParams.batchSize] = values[HttpParams.pagingBatchSize]

    values[HttpParams.pageing] = paging
    return values
  },

  formatPrimaryKeys: (values: Record<string, any>) => {
    const primaryKeys: Array<string> = []
    values[JdbcParams.primaryKeyArray]?.forEach(function (item: Record<string, any>) {
      primaryKeys.push(item[JdbcParams.primaryKey])
    });
    values[JdbcParams.primaryKeys] = JSON.stringify(primaryKeys)
    return values
  },

  formatMeasurementFields: (values: Record<string, any>) => {
    const primaryKeys: Array<string> = []
    values[IoTDBParams.keyMeasurementFieldArray]?.forEach(function (item: Record<string, any>) {
      primaryKeys.push(item[IoTDBParams.keyMeasurementField])
    });
    values[IoTDBParams.keyMeasurementFields] = JSON.stringify(primaryKeys)
    return values
  },

  formatStarRocksConfig: (values: Record<string, any>) => {
    const config: Record<string, any> = {}
    values[StarRocksParams.starrocksConfigMap]?.forEach(function (item: Record<string, any>) {
      values[item[StarRocksParams.starrocksConfigKey]] = item[StarRocksParams.starrocksConfigValue];
    });
    values[StarRocksParams.starrocksConfig] = JSON.stringify(config)
    return values
  },

  formatKeyTags: (values: Record<string, any>) => {
    values[InfluxDBParams.keyTags] = JSON.stringify(values[InfluxDBParams.keyTagArray])
    return values
  },

  formatFieldMapper: (values: Record<string, any>) => {
    const fieldMapper: Record<string, any> = {}
    values[FieldMapperParams.fieldMapperGroup]?.forEach(function (item: Record<string, any>) {
      fieldMapper[item[FieldMapperParams.srcField]] = item[FieldMapperParams.destField];
    });
    values[FieldMapperParams.fieldMapper] = JSON.stringify(fieldMapper)
    return values
  },

  formatFilterFields: (values: Record<string, any>) => {
    const fields: Array<string> = []
    values[FilterParams.fieldArray]?.forEach(function (item: Record<string, any>) {
      fields.push(item[FilterParams.field])
    });
    values[FilterParams.fields] = JSON.stringify(fields)
    return values
  },

  formatSplitOutputFields: (values: Record<string, any>) => {
    const outputFields: Array<string> = []
    values[SplitParams.outputFieldArray]?.forEach(function (item: Record<string, any>) {
      outputFields.push(item[SplitParams.outputField])
    });
    values[SplitParams.outputFields] = JSON.stringify(outputFields)
    return values
  },

  formatRocketMQPartitionOffsets: (values: Record<string, any>) => {
    const paritionOffsets: Record<string, any> = {}
    values[RocketMQParams.startModeOffsetsList]?.forEach(function (item: Record<string, any>) {
      paritionOffsets[item[RocketMQParams.specificPartition]] = item[RocketMQParams.specificPartitionOffset];
    });
    values[RocketMQParams.startModeOffsets] = JSON.stringify(paritionOffsets)
    return values
  },

  formatIcebergCatalogConfig: (values: Record<string, any>) => {
    const catalogConfig: Record<string, any> = {}
    catalogConfig[IcebergParams.catalogConfigType] = values[IcebergParams.catalogConfigType]
    catalogConfig[IcebergParams.catalogConfigUri] = values[IcebergParams.catalogConfigUri]
    catalogConfig[IcebergParams.catalogConfigWarehouse] = values[IcebergParams.catalogConfigWarehouse]
    values[IcebergParams.catalogConfig] = JSON.stringify(catalogConfig)
    return values
  },

};
