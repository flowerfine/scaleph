import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const CassandraForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "host"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.host'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.host.placeholoder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost:9042"}
        />
        <ProFormText
          name={[prefix, "keyspace"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.keyspace'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"test"}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.username'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.password'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "datacenter"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.cassandra.datacenter'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default CassandraForm;

