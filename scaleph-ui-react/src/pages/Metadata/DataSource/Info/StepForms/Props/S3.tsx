import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const S3Form: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem type={type}/>
        <ProFormText
          name="bucket"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.s3.bucket'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="accessKey"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.s3.accessKey'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="accessSecret"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.s3.accessSecret'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default S3Form;

