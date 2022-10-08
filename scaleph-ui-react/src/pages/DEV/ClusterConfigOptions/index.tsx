import {FlinkClusterConfig, FlinkClusterConfigAddParam} from '@/services/dev/typings';
import {ProCard, ProFormInstance, StepsForm,} from '@ant-design/pro-components';
import {useIntl, useLocation} from 'umi';
import {useRef, useState} from "react";
import FlinkImageOptions from "@/pages/DEV/ClusterConfigOptions/components/FlinkImage";
import K8sResourceOptions from "@/pages/DEV/ClusterConfigOptions/components/K8sResource";
import BaseOptions from "@/pages/DEV/ClusterConfigOptions/components/Base";
import {FlinkClusterConfigService} from "@/services/dev/flinkClusterConfig.service";

const ClusterConfigOptionsSteps: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const [flinkClusterConfig, setFlinkClusterConfig] = useState<FlinkClusterConfig>({});

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={async (values) => {

        }}>
        <StepsForm.StepForm
          name="base"
          title={(intl.formatMessage({id: 'pages.dev.clusterConfig.baseStep'}))}
          layout={'horizontal'}
          style={{width: 1000}}
          onFinish={(values) => {
            const param: FlinkClusterConfigAddParam = {...values}
            return FlinkClusterConfigService.add(param).then((response) => {
              setFlinkClusterConfig(response.data ? response.data : {})
              return response.success
            })
          }}
        >
          <BaseOptions/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="kubernetes"
          title={(intl.formatMessage({id: 'pages.dev.clusterConfig.kubernetesStep'}))}
          layout={'horizontal'}
          style={{width: 1000}}
          stepProps={{
            description: intl.formatMessage({id: 'pages.dev.clusterConfig.kubernetesStep.description'})
          }}
        >
          <FlinkImageOptions/>
          <K8sResourceOptions/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default ClusterConfigOptionsSteps;
