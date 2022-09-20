import {useIntl} from "umi";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";

const Additional: React.FC = () => {
  const intl = useIntl();

  return (<ProCard title={"Additional Config Options"} headerBordered collapsible={true}>
    <ProFormList
      name="options"
      copyIconProps={false}
      colProps={{span: 21, offset: 1}}
      creatorButtonProps={{
        creatorButtonText: intl.formatMessage({id: 'pages.dev.clusterConfig.configOptions'}),
        type: "text",
      }}
    >
      <ProFormGroup>
        <ProFormText name="key" label={intl.formatMessage({id: 'pages.dev.clusterConfig.configOptions.key'})} colProps={{span: 10, offset: 1}}/>
        <ProFormText name="value" label={intl.formatMessage({id: 'pages.dev.clusterConfig.configOptions.value'})} colProps={{span: 10, offset: 1}}/>
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default Additional;
