import {ProCard, ProForm, StepsForm,} from '@ant-design/pro-components';
import {Form} from 'antd';
import JobJar from "@/pages/DEV/JobConfig/components/JobJar";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";
import {useIntl} from "umi";

const JobConfigJarOptions: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <ProCard>
        <StepsForm
          formProps={{
            grid: true
          }}
        >
          <StepsForm.StepForm
            name="artifact"
            title="Artifact"
            layout={"horizontal"}
            style={{
              width: 1000
            }}>
            <JobJar/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="configOptions"
            title="Config Options"
            layout={"horizontal"}
            style={{
              width: 1000
            }}>
            <JobClusterConfigOptions/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="confirm"
            title="Confirm"
            layout={"horizontal"}
            style={{
              width: 1000
            }}>

          </StepsForm.StepForm>
        </StepsForm>
    </ProCard>
  );
};

export default JobConfigJarOptions;
