import {useIntl} from "umi";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";

const AdvancedAdditional: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={"Additional Config Options"}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormList
      name="options"
      copyIconProps={false}
      colProps={{span: 21, offset: 1}}
      creatorButtonProps={{
        creatorButtonText: intl.formatMessage({id: 'page.project.cluster.config.configOptions'}),
        type: "text",
      }}
    >
      <ProFormGroup>
        <ProFormText
          name="key"
          label={intl.formatMessage({id: 'page.project.cluster.config.configOptions.key'})}
          colProps={{span: 10, offset: 1}}
        />
        <ProFormText
          name="value"
          label={intl.formatMessage({id: 'page.project.cluster.config.configOptions.value'})}
          colProps={{span: 10, offset: 1}}
        />
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default AdvancedAdditional;
