export const StepSchemaService = {

  formatSchema: (values: Record<string, any>) => {
    const fields: Record<string, any> = {}
    values.fields?.forEach(function (item: Record<string, any>) {
      fields[item.field] = item.type;
    });
    values.schema = JSON.stringify({fields: fields})
    return values
  },

  formatHeader: (values: Record<string, any>) => {
    const headers: Record<string, any> = {}
    values.headerArray?.forEach(function (item: Record<string, any>) {
      headers[item.header] = item.value;
    });
    values.headers = JSON.stringify(headers)
    return values
  },

  formatParam: (values: Record<string, any>) => {
    const params: Record<string, any> = {}
    values.paramArray?.forEach(function (item: Record<string, any>) {
      params[item.param] = item.value;
    });
    values.params = JSON.stringify(params)
    return values
  },
};
