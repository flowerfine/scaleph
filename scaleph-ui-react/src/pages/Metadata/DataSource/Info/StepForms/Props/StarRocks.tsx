import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const StarRocksForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "nodeUrls"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.starrocks.nodeUrls'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.starrocks.nodeUrls.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost:8030"}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.starrocks.username'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.starrocks.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default StarRocksForm;

