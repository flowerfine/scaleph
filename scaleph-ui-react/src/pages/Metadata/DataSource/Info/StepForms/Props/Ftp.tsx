import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const FtpForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "host"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.host'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost"}
        />
        <ProFormDigit
          name={[prefix, "port"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.port'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={21}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.username'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default FtpForm;

