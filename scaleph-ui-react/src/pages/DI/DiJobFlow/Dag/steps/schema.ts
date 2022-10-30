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

};
