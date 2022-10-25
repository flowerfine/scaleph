import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {DiJob} from "@/services/project/typings";

const SeaTunnelArtifactOptions: React.FC<{ data: DiJob }> = ({data}) => {
  const intl = useIntl();
  return (
    <ProCard
      title={intl.formatMessage({id: 'pages.dev.job.seatunnel'})}
      headerBordered
      collapsible={true}
      style={{width: 1000}}>
      <ProFormGroup>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.project.di.jobName'})}
          colProps={{span: 21, offset: 1}}
          readonly={true}
          initialValue={data.jobName}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'pages.project.di.remark'})}
          colProps={{span: 21, offset: 1}}
          readonly={true}
          initialValue={data.remark}
          rules={[{max: 200}]}/>
      </ProFormGroup>

    </ProCard>);
}

export default SeaTunnelArtifactOptions;
