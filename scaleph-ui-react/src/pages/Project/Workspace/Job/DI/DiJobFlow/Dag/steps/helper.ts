import {
  CassandraParams, DorisParams,
  ElasticsearchParams,
  IoTDBParams,
  JdbcParams,
  KafkaParams,
  SchemaParams, StarRocksParams
} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/constant";

export const StepSchemaService = {

  formatSchema: (values: Record<string, any>) => {
    const fields: Record<string, any> = {}
    values.fields?.forEach(function (item: Record<string, any>) {
      fields[item.field] = item.type;
    });
    values.schema = JSON.stringify({fields: fields})
    return values
  },

  formatFields: (values: Record<string, any>) => {
    const fields: Record<string, any> = {}
    values[SchemaParams.fieldArray]?.forEach(function (item: Record<string, any>) {
      fields[item.field] = item.type;
    });
    values[SchemaParams.fields] = JSON.stringify(fields)
    return values
  },

  formatHeader: (values: Record<string, any>) => {
    const headers: Record<string, any> = {}
    values.headerArray?.forEach(function (item: Record<string, any>) {
      headers[item.header] = item.headerValue;
    });
    values.headers = JSON.stringify(headers)
    return values
  },

  formatParam: (values: Record<string, any>) => {
    const params: Record<string, any> = {}
    values.paramArray?.forEach(function (item: Record<string, any>) {
      params[item.param] = item.paramValue;
    });
    values.params = JSON.stringify(params)
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
    values.kafkaConf?.forEach(function (item: Record<string, any>) {
      values['kafka.' + item.key] = item.value;
    });
    return values
  },

  formatPartitionKeyFields: (values: Record<string, any>) => {
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

  formatClickHouseConf: (values: Record<string, any>) => {
    values.clickhouse_conf?.forEach(function (item: Record<string, any>) {
      values['clickhouse.' + item.key] = item.value;
    });
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

  formatJsonField: (values: Record<string, any>) => {
    const jsonFields: Record<string, any> = {}
    values.jsonField?.forEach(function (item: Record<string, any>) {
      jsonFields[item.key] = item.path;
    });
    values.json_field = JSON.stringify(jsonFields)
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

  formatEsPrimaryKeys: (values: Record<string, any>) => {
    const primaryKeys: Array<string> = []
    values[ElasticsearchParams.primaryKeyArray]?.forEach(function (item: Record<string, any>) {
      primaryKeys.push(item[ElasticsearchParams.primaryKey])
    });
    values[ElasticsearchParams.primaryKeys] = JSON.stringify(primaryKeys)
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

  formatCassandraFields: (values: Record<string, any>) => {
    const primaryKeys: Array<string> = []
    values[CassandraParams.fieldArray]?.forEach(function (item: Record<string, any>) {
      primaryKeys.push(item[CassandraParams.field])
    });
    values[CassandraParams.fields] = JSON.stringify(primaryKeys)
    return values
  },

  formatDorisSinkProperties: (values: Record<string, any>) => {
    values[DorisParams.sinkPropertyArray]?.forEach(function (item: Record<string, any>) {
      values[DorisParams.sinkProperties + item[DorisParams.sinkProperty]] = item[DorisParams.sinkPropertyValue];
    });
    return values
  },

  formatStarRocksSinkProperties: (values: Record<string, any>) => {
    values[StarRocksParams.sinkPropertyArray]?.forEach(function (item: Record<string, any>) {
      values[StarRocksParams.sinkProperties + item[StarRocksParams.sinkProperty]] = item[StarRocksParams.sinkPropertyValue];
    });
    return values
  },

};
