import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";
import {useIntl} from "@@/exports";
import {useRef} from "react";
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

        }}
      >
        <KubernetesStep/>
      </StepsForm>
    </ProCard>
  );
}

export default KubernetesOptions;

