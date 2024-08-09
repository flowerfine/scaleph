import React from "react";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {DataSourceProps} from "@/services/datasource/typings";
import CommonItem from "@/pages/Metadata/DataSource/Info/StepForms/Props/CommonProps";

const JdbcForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <CommonItem prefix={prefix} type={type}/>
        <ProFormText
          name={[prefix, "driverClassName"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.driverClassName'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "url"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.url'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "user"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.user'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormList
          name="additionalProps"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.additionalProps'})}
          copyIconProps={false}
          colProps={{span: 21, offset: 1}}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.metadata.dataSource.step.props.jdbc.additionalProps.name'}),
            type: "text",
          }}
        >
          <ProFormGroup>
            <ProFormText name="property"
                         label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps.property'})}
                         colProps={{span: 10, offset: 1}}/>
            <ProFormText name="value"
                         label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps.value'})}
                         colProps={{span: 10, offset: 1}}/>
          </ProFormGroup>
        </ProFormList>
      </ProCard>
    </div>
  );
}

export default JdbcForm;
