import {ProCard, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl, useModel} from "@umijs/max";
import {DsCategoryService} from "@/services/datasource/category.service";
import {useEffect} from "react";
import {Form} from "antd";

const ClickHouseForm: React.FC = () => {
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
        <ProFormText
          name="host"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.host'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost:8123"}
        />
        <ProFormText
          name="database"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.database'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"default"}
        />
        <ProFormText
          name="username"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.username'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"default"}
        />
        <ProFormText
          name="password"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={""}
        />
      </ProCard>
    </div>
  );
}

export default ClickHouseForm;
