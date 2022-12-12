import {useIntl, useModel} from "umi";
import {Form} from "antd";
import {useEffect} from "react";
import {ProCard, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsCategoryService} from "@/services/datasource/category.service";

const PulsarForm: React.FC = () => {
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
          name="webServiceUrl"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.webServiceUrl'})}
          placeholder={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.webServiceUrl.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="clientServiceUrl"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.clientServiceUrl'})}
          placeholder={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.clientServiceUrl.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="authPlugin"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.authPlugin'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name="authParams"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.authParams'})}
          placeholder={intl.formatMessage({id: 'pages.dataSource.step.props.pulsar.authParams.placeholder'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default PulsarForm;

