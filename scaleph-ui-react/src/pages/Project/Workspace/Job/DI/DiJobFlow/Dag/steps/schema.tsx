import {getIntl, getLocale} from "umi";
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormDependency, ProFormGroup, ProFormList, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {SchemaParams} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/constant";

const SchemaItem: React.FC = () => {
  const intl = getIntl(getLocale(), true);
  return (
    <ProFormGroup>
      <ProFormSelect
        name={'type'}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.type'})}
        rules={[{required: true}]}
        valueEnum={{
          json: 'json',
          parquet: 'parquet',
          orc: 'orc',
          text: 'text',
          csv: 'csv',
        }}
      />
      <ProFormDependency name={['type']}>
        {({type}) => {
          if (type == 'json') {
            return (
              <ProFormGroup
                label={intl.formatMessage({id: 'pages.project.di.step.schema'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
              >
                <ProFormList
                  name={SchemaParams.fields}
                  copyIconProps={false}
                  creatorButtonProps={{
                    creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.schema.fields'}),
                    type: 'text',
                  }}
                >
                  <ProFormGroup>
                    <ProFormText
                      name={SchemaParams.field}
                      label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.field'})}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name={SchemaParams.type}
                      label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.type'})}
                      colProps={{span: 10, offset: 1}}
                    />
                  </ProFormGroup>
                </ProFormList>
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
      <ProFormText
        name={SchemaParams.delimiter}
        label={intl.formatMessage({id: 'pages.project.di.step.schema.delimiter'})}
        initialValue={'\\001'}
      />
    </ProFormGroup>
  );
}

export default SchemaItem;
