import React, {useEffect} from 'react';
import {Form} from "antd";
import {ProFormGroup, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {DsCategoryService} from "@/services/datasource/category.service";
import {DataSourceProps, DsType} from "@/services/datasource/typings";

const CommonItem: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();
  const form = Form.useFormInstance()

  useEffect(() => {
    form.setFieldValue([prefix, "type"], type?.type?.value)
    form.setFieldValue("dsTypeId", type?.id)
  }, [])

  return (
    <ProFormGroup>
      <ProFormText name={[prefix, "type"]} hidden/>
      <ProFormSelect
        name="dsTypeId"
        label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.type'})}
        colProps={{span: 21, offset: 1}}
        disabled
        showSearch={false}
        request={() => {
          return DsCategoryService.listTypes({}).then((response) => {
            if (response.data) {
              return response.data.map((item) => {
                return {label: item.type.label, value: item.id, item: item};
              });
            }
            return []
          })
        }}
      />
      <ProFormText
        name="version"
        label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.version'})}
        colProps={{span: 21, offset: 1}}
      />
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.name'})}
        colProps={{span: 21, offset: 1}}
        rules={[{required: true}]}
      />
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
        colProps={{span: 21, offset: 1}}
        fieldProps={{
          rows: 5
        }}
      />
    </ProFormGroup>
  );
}

export default CommonItem;
