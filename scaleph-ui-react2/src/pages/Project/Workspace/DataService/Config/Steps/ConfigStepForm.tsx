import React from "react";
import {
  ProCard,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DataserviceConfigConfigStep: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormGroup
        title={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap'})}
      >
        <ProFormList
          name={"parameterMappings"}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap.parameterMapping'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={"property"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap.parameterMapping.property'})}
              colProps={{span: 4, offset: 1}}
              rules={[{required: true}]}
            />
            <ProFormText
              name={"javaType"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap.parameterMapping.javaType'})}
              colProps={{span: 4, offset: 1}}
              rules={[{required: true}]}
            />
            <ProFormText
              name={"jdbcType"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap.parameterMapping.jdbcType'})}
              colProps={{span: 4, offset: 1}}
            />
            <ProFormText
              name={"typeHandler"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.parameterMap.parameterMapping.typeHandler'})}
              colProps={{span: 8, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
      </ProFormGroup>

      <ProFormGroup
        title={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap'})}
      >
        <ProFormList
          name={"resultMappings"}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={"property"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping.property'})}
              colProps={{span: 3, offset: 1}}
              rules={[{required: true}]}
            />
            <ProFormText
              name={"column"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping.column'})}
              colProps={{span: 3, offset: 1}}
              rules={[{required: true}]}
            />
            <ProFormText
              name={"javaType"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping.javaType'})}
              colProps={{span: 3, offset: 1}}
            />
            <ProFormText
              name={"jdbcType"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping.jdbcType'})}
              colProps={{span: 3, offset: 1}}
            />
            <ProFormText
              name={"typeHandler"}
              label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.resultMap.resultMapping.typeHandler'})}
              colProps={{span: 7, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
      </ProFormGroup>

      <ProFormGroup
        title={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.sql'})}
      >
        <ProFormSelect
          name={"type"}
          label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.sql.type'})}
          rules={[{required: true}]}
          options={["select", "upsert", "insert", "update", "delete"]}
        />
        <ProFormTextArea
          name={"query"}
          label={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config.sql.query'})}
          rules={[{required: true}]}
          fieldProps={{
            rows: 15
          }}
        />
      </ProFormGroup>
    </ProCard>
  );
}

export default DataserviceConfigConfigStep;
