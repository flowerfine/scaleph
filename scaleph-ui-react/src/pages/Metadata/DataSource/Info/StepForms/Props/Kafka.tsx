import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const KafkaForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "bootstrapServers"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.kafka.bootstrapServers'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.kafka.bootstrapServers.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default KafkaForm;

