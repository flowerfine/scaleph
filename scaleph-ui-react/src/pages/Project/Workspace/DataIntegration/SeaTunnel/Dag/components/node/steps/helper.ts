import {
  ColumnParams,
  CommonConfigParams,
  CommonListParams,
  FieldMapperParams,
  FilterParams,
  HttpParams,
  IcebergParams,
  SchemaParams,
  SplitParams
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
    if (list.length > 0) {
      values[param] = JSON.stringify(list)
    }
    return values
  },

  formatCommonConfig: (values: Record<string, any>, param: string, prefix: string) => {
    const configs: Record<string, any> = {}
    values[prefix + CommonConfigParams.commonConfig]?.forEach(function (item: Record<string, any>) {
      configs[item[prefix + CommonConfigParams.commonConfigKey]] = item[prefix + CommonConfigParams.commonConfigValue];
    });
    if (isRecordNotEmpty(configs)) {
      values[param] = JSON.stringify(configs)
    }
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

  formatIcebergCatalogConfig: (values: Record<string, any>) => {
    const catalogConfig: Record<string, any> = {}
    catalogConfig[IcebergParams.catalogConfigType] = values[IcebergParams.catalogConfigType]
    catalogConfig[IcebergParams.catalogConfigUri] = values[IcebergParams.catalogConfigUri]
    catalogConfig[IcebergParams.catalogConfigWarehouse] = values[IcebergParams.catalogConfigWarehouse]
    values[IcebergParams.catalogConfig] = JSON.stringify(catalogConfig)
    return values
  },

};

function isRecordNotEmpty<T>(record: Record<string, T>): boolean {
  for (const key in record) {
    if (record.hasOwnProperty(key)) {
      return true;
    }
  }
  return false;
}
