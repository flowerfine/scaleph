import JobClusterConfigOptions from '@/pages/DEV/JobConfig/components/ClusterConfigOptions';
import {ProCard, ProFormInstance, StepsForm} from '@ant-design/pro-components';
import {useRef} from 'react';
import {useIntl, useLocation} from 'umi';
import SeaTunnelJob from "@/pages/DEV/JobConfig/components/SeaTunnelJob";
import {DiJob} from "@/services/project/typings";

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
          console.log('JobConfigSeaTunnelOptions', values)
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
