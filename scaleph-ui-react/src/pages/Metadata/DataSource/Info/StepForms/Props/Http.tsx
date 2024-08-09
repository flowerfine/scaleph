import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const HttpForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormSelect
          name={[prefix, "method"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.http.method'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"GET"}
          options={["GET", "POST"]}
        />
        <ProFormText
          name={[prefix, "url"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.http.url'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default HttpForm;

