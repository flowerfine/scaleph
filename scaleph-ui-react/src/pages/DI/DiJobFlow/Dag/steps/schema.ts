export const StepSchemaService = {

  formatSchema: (values: Record<string, any>) => {
    const fields: Map<string, any> = new Map<string, any>()
    values.fields?.forEach(function (item: Record<string, any>) {
      fields[item.key] = item.value;
    });
    values.schema = JSON.stringify({fields: fields})
    return values
  },
};
