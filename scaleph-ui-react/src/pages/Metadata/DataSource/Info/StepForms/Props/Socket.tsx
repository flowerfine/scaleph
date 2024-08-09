import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const SocketForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem type={type}/>
        <ProFormText
          name={"host"}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.socket.host'})}
          rules={[{required: true}]}
          colProps={{span: 21, offset: 1}}
          initialValue={"127.0.0.1"}
        />
        <ProFormDigit
          name={"port"}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.socket.port'})}
          rules={[{required: true}]}
          colProps={{span: 21, offset: 1}}
          initialValue={9999}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
      </ProCard>
    </div>
  );
}

export default SocketForm;

