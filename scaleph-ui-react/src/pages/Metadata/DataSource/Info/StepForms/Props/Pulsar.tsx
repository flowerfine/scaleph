import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const PulsarForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "webServiceUrl"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.webServiceUrl'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.webServiceUrl.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "clientServiceUrl"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.clientServiceUrl'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.clientServiceUrl.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "authPlugin"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.authPlugin'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "authParams"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.authParams'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.pulsar.authParams.placeholder'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default PulsarForm;

