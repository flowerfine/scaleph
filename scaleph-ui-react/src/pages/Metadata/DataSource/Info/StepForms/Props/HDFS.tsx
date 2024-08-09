import {useIntl} from "@umijs/max";
import React from "react";
import {ProCard, ProFormText} from "@ant-design/pro-components";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const HDFSForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "hdfsSitePath"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.hdfs.hdfsSitePath'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name={[prefix, "fsDefaultFS"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.hdfs.fsDefaultFs'})}
          placeholder={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.hdfs.fsDefaultFs.placeholder'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default HDFSForm;

