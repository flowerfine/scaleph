import {getIntl, getLocale} from "umi";
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {ColumnParams} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/constant";

const ColumnItem: React.FC = () => {
  const intl = getIntl(getLocale(), true);
  return (
    <ProFormGroup
      label={intl.formatMessage({id: 'pages.project.di.step.column'})}
      tooltip={{
        title: intl.formatMessage({id: 'pages.project.di.step.column.tooltip'}),
        icon: <InfoCircleOutlined/>,
      }}
    >
      <ProFormList
        name={ColumnParams.readColumnArray}
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.column.field'}),
          type: 'text',
        }}
      >
        <ProFormGroup>
          <ProFormText
            name={ColumnParams.readColumn}
            colProps={{span: 20, offset: 2}}
          />
        </ProFormGroup>
      </ProFormList>
    </ProFormGroup>
  );
}

export default ColumnItem;
