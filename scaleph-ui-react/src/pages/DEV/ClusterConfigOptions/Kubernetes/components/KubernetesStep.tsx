import {StepsForm} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";
import FlinkImageOptions from "@/pages/DEV/ClusterConfigOptions/components/FlinkImage";
import K8sResourceOptions from "@/pages/DEV/ClusterConfigOptions/components/K8sResource";

const KubernetesStep: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  return (<StepsForm.StepForm
    name="kubernetes"
    title={"Kubernetes"}
    layout={'horizontal'}
    style={{
      width: 1000,
    }}
  >
    <FlinkImageOptions/>
    <K8sResourceOptions/>
  </StepsForm.StepForm>);
}

export default KubernetesStep;
