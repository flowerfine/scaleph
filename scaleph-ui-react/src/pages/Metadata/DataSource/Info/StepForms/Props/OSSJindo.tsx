import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const OSSJindoForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "endpoint"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ossjindo.endpoint'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "bucket"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ossjindo.bucket'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "accessKey"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ossjindo.accessKey'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "accessSecret"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ossjindo.accessSecret'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default OSSJindoForm;

