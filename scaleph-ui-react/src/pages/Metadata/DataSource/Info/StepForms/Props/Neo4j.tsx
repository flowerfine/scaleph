import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const Neo4jForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "uri"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.neo4j.uri'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"neo4j://localhost:7687"}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.neo4j.username'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.neo4j.password'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "bearerToken"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.neo4j.bearerToken'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "kerberosTicket"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.neo4j.kerberosTicket'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default Neo4jForm;

