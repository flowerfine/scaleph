import JobClusterConfigOptions from '@/pages/DEV/JobConfig/components/ClusterConfigOptions';
import {ProCard, ProFormInstance, StepsForm} from '@ant-design/pro-components';
import {useRef} from 'react';
import {useIntl, useLocation} from 'umi';
import SeaTunnelJob from "@/pages/DEV/JobConfig/components/SeaTunnelJob";
import {DiJob} from "@/services/project/typings";
import {FlinkClusterConfigService} from "@/services/dev/flinkClusterConfig.service";
import {FlinkJob} from "@/pages/DEV/Job/typings";
import {FlinkJobService} from "@/pages/DEV/Job/FlinkJobService";
import {message} from "antd";
import {history} from "@@/core/history";

const JobConfigSeaTunnelOptions: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const diJob = urlParams.state as DiJob

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true
        }}
        onFinish={async (values) => {
          const jobConfig = FlinkClusterConfigService.formatArgs(values)
          const jars = FlinkClusterConfigService.formatJars(values)
          const param: FlinkJob = {
            type: '2',
            name: values['name'],
            flinkArtifactId: diJob.id,
            jobConfig: jobConfig,
            flinkClusterConfigId: values['flinkClusterConfig'],
            flinkClusterInstanceId: values['flinkClusterInstance'],
            flinkConfig: FlinkClusterConfigService.getData(values),
            jars: jars,
            remark: values['remark'],
          };
          FlinkJobService.add(param)
            .then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                history.push("/workspace/dev/job/seatunnel")
              }
            })
        }}
      >
        <StepsForm.StepForm
          name="seatunnel"
          title={intl.formatMessage({id: 'pages.dev.job.seatunnel'})}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <SeaTunnelJob data={diJob}/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="configOptions"
          title={intl.formatMessage({id: 'pages.dev.job.configOptions'})}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
        >
          <JobClusterConfigOptions/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default JobConfigSeaTunnelOptions;
