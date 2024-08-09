import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const InfluxDBForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "url"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.influxdb.url'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"http://influxdb-host:8086"}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.influxdb.username'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.influxdb.password'})}
          colProps={{span: 21, offset: 1}}
        />
      </ProCard>
    </div>
  );
}

export default InfluxDBForm;

