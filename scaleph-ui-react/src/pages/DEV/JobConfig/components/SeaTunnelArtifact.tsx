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
          name="directory"
          label={intl.formatMessage({id: 'pages.project.di.directory'})}
          colProps={{span: 21, offset: 1}}
          readonly={true}
          initialValue={data.directory?.fullPath}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="jobName"
          label={intl.formatMessage({id: 'pages.project.di.jobName'})}
          colProps={{span: 21, offset: 1}}
          readonly={true}
          initialValue={data.jobName}
          rules={[{required: true}, {max: 128}]}/>
      </ProFormGroup>

    </ProCard>);
}

export default SeaTunnelArtifactOptions;
