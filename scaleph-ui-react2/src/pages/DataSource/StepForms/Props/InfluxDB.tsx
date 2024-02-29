import {useIntl, useModel} from "umi";
import {Form} from "antd";
import {useEffect} from "react";
import {ProCard, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsCategoryService} from "@/services/datasource/category.service";

const InfluxDBForm: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance()

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  useEffect(() => {
    form.setFieldValue("dsTypeId", dsType?.id)
  }, [dsType])

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <ProFormSelect
          name="dsTypeId"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.type'})}
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
          label={intl.formatMessage({id: 'pages.dataSource.step.props.version'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.name'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({id: 'pages.dataSource.remark'})}
          colProps={{span: 21, offset: 1}}
          fieldProps={{
            rows: 5
          }}
        />
        <ProFormText
          name="url"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.influxdb.url'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"http://influxdb-host:8086"}
        />
        <ProFormText
          name="username"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.influxdb.username'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name="password"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.influxdb.password'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default InfluxDBForm;

