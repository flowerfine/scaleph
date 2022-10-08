import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {useRef} from "react";
import FlinkImageOptions from "@/pages/DEV/ClusterConfigOptions/components/FlinkImage";
import K8sResourceOptions from "@/pages/DEV/ClusterConfigOptions/components/K8sResource";
import KubernetesStep from "@/pages/DEV/ClusterConfigOptions/Kubernetes/components/KubernetesStep";

const KubernetesOptions: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
        }}
        onFinish={async (values) => {

        }}>

        <StepsForm.StepForm
          name="kubernetes"
          title={"Kubernetes"}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
          stepProps={{
            description: "自定义描述"
          }}>
          <FlinkImageOptions/>
          <K8sResourceOptions/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="configOptions"
          title={"configOptions"}
          layout={'horizontal'}
          style={{
            width: 1000,
          }}
          stepProps={{
            description: "自定义描述"
          }}
        >
          <FlinkImageOptions/>
          <K8sResourceOptions/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
}

export default KubernetesOptions;

