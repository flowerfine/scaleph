import {useIntl} from "umi";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";

const SeaTunnelEnvOptions: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard
      title={intl.formatMessage({id: 'pages.dev.job.seatunnel.env'})}
      headerBordered
      collapsible={true}>
      <ProFormList
        name="options"
        copyIconProps={false}
        colProps={{span: 21, offset: 1}}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.dev.job.seatunnel.env.new'}),
          type: "text",
        }}>
        <ProFormGroup>
          <ProFormText
            name="key"
            label={intl.formatMessage({id: 'pages.dev.job.seatunnel.env.key'})}
            colProps={{span: 10, offset: 1}}/>
          <ProFormText
            name="value"
            label={intl.formatMessage({id: 'pages.dev.job.seatunnel.env.value'})}
            colProps={{span: 10, offset: 1}}/>
        </ProFormGroup>
      </ProFormList>
    </ProCard>);
}

export default SeaTunnelEnvOptions;
