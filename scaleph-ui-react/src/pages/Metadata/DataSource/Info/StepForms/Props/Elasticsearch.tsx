import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const ElasticsearchForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "hosts"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.elasticsearch.hosts'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.elasticsearch.hosts.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.elasticsearch.username'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.elasticsearch.password'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default ElasticsearchForm;

