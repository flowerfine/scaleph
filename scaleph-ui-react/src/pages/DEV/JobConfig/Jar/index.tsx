import {StepsForm} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import JobJar from "@/pages/DEV/JobConfig/components/JobJar";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";

const JobConfigJarOptions: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <StepsForm
      formProps={{
        form: form,
        validateMessages: {
          required: '此项为必填项',
        },
      }}>
      <StepsForm.StepForm<{name: string}>
        name="artifact"
        title="Artifact"
        layout={"horizontal"}>
        <JobJar></JobJar>
      </StepsForm.StepForm>

      <StepsForm.StepForm
        name="configOptions"
        title="Config Options"
        layout={"horizontal"}>
        <JobClusterConfigOptions></JobClusterConfigOptions>
      </StepsForm.StepForm>

      <StepsForm.StepForm
        name="time"
        title="第三步骤">
      </StepsForm.StepForm>

    </StepsForm>
  );
}

export default JobConfigJarOptions;
