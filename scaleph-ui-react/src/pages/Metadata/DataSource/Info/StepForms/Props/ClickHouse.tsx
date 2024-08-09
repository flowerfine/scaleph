import {ProCard, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import React from "react";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const ClickHouseForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "host"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.host'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost:8123"}
        />
        <ProFormText
          name={[prefix, "database"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.database'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"default"}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.clickhouse.username'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"default"}
        />
        <ProFormText
          name={[prefix, "password"]}
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
