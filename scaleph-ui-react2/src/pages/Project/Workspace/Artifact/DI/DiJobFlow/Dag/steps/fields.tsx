import {getIntl, getLocale} from "umi";
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {SchemaParams} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/constant";

const FieldItem: React.FC = () => {
  const intl = getIntl(getLocale(), true);
  return (
    <ProFormGroup
      label={intl.formatMessage({id: 'pages.project.di.step.schema'})}
      tooltip={{
        title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
        icon: <InfoCircleOutlined/>,
      }}
    >
      <ProFormList
        name={SchemaParams.fieldArray}
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

export default FieldItem;
