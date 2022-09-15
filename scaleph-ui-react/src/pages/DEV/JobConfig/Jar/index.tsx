import {ProCard, StepsForm,} from '@ant-design/pro-components';
import {Form} from 'antd';
import JobJar from "@/pages/DEV/JobConfig/components/JobJar";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";
import {useIntl} from "umi";

const JobConfigJarOptions: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <ProCard>
      <StepsForm>
        <StepsForm.StepForm name="artifact" title="Artifact" layout={"horizontal"}>
          <JobJar/>
        </StepsForm.StepForm>
        <StepsForm.StepForm name="configOptions" title="Config Options" layout={"horizontal"}>
          <JobClusterConfigOptions/>
        </StepsForm.StepForm>
        <StepsForm.StepForm name="time" title="发布实验" layout={"horizontal"}>

        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default JobConfigJarOptions;
