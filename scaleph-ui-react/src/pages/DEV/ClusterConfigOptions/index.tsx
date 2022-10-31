import {FlinkClusterConfig, FlinkClusterConfigAddParam, KubernetesOptions} from '@/services/dev/typings';
import {ProCard, ProFormInstance, StepsForm,} from '@ant-design/pro-components';
import {history, useIntl, useLocation} from 'umi';
import {useRef, useState} from "react";
import BaseOptions from "@/pages/DEV/ClusterConfigOptions/components/Base";
import {FlinkClusterConfigService} from "@/services/dev/flinkClusterConfig.service";
import State from "@/pages/DEV/ClusterConfigOptions/components/State";
import FaultTolerance from "@/pages/DEV/ClusterConfigOptions/components/FaultTolerance";
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";
import Resource from "@/pages/DEV/ClusterConfigOptions/components/Resource";
import Additional from "@/pages/DEV/ClusterConfigOptions/components/Additional";

const ClusterConfigOptionsSteps: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const [flinkClusterConfig, setFlinkClusterConfig] = useState<FlinkClusterConfig>({});

  const params = urlParams.state as FlinkClusterConfig;

  const baseData = {
    name: params?.name,
    resourceProvider: params?.resourceProvider?.value,
    deployMode: params?.deployMode?.value,
    clusterCredentialId: params?.clusterCredential?.id,
    flinkVersion: params?.flinkVersion?.value,
    flinkReleaseId: params?.flinkRelease?.id,
    remark: params?.remark,
  };
  const kubernetesOptions = params.kubernetesOptions
  const configOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(params?.configOptions ? params?.configOptions : {}))
  );

  const add = (values: Record<string, any>) => {
    const param: FlinkClusterConfigAddParam = {...values}
    return FlinkClusterConfigService.add(param).then((response) => {
      setFlinkClusterConfig(response.data ? response.data : {})
      return response.success
    })
  }

  const update = (values: Record<string, any>) => {
    const param: FlinkClusterConfig = {
      id: params?.id,
      name: values.name,
      resourceProvider: values.resourceProvider,
      deployMode: values.deployMode,
      clusterCredential: {id: values.clusterCredentialId},
      flinkVersion: values.flinkVersion,
      flinkRelease: {id: values.flinkReleaseId},
      remark: values.remark
    };
    return FlinkClusterConfigService.update(param).then((response) => {
      setFlinkClusterConfig(response.data ? response.data : {})
      return response.success
    })
  }

  const updateKubernetesOptions = (values: Record<string, any>) => {
    const id = params?.id ? params?.id : flinkClusterConfig.id;
    const param: KubernetesOptions = {...values}
    return FlinkClusterConfigService.updateKubernetesOptions(id, param)
      .then((response) => response.success)
  }

  const updateConfigOptions = (values: Record<string, any>) => {
    const id = params?.id ? params?.id : flinkClusterConfig.id;
    const param = FlinkClusterConfigService.getData(values)
    return FlinkClusterConfigService.updateConfigOptions(id, param)
      .then((response) => response.success)
  }

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={async (values) => {
          history.back();
        }}>
        <StepsForm.StepForm
          name="base"
          title={(intl.formatMessage({id: 'pages.dev.clusterConfig.baseStep'}))}
          layout={'horizontal'}
          style={{width: 1000}}
          initialValues={baseData}
          onFinish={(values) => {
            return params?.id ? update(values) : add(values)
          }}
        >
          <BaseOptions/>
        </StepsForm.StepForm>
        {/*<StepsForm.StepForm*/}
        {/*  name="kubernetes"*/}
        {/*  title={(intl.formatMessage({id: 'pages.dev.clusterConfig.kubernetesStep'}))}*/}
        {/*  layout={'horizontal'}*/}
        {/*  style={{width: 1000}}*/}
        {/*  initialValues={kubernetesOptions}*/}
        {/*  onFinish={updateKubernetesOptions}*/}
        {/*>*/}
        {/*  <K8sBaseOptions/>*/}
        {/*  <FlinkImageOptions/>*/}
        {/*  <K8sResourceOptions/>*/}
        {/*</StepsForm.StepForm>*/}
        <StepsForm.StepForm
          name="configOptions"
          title={(intl.formatMessage({id: 'pages.dev.clusterConfig.configOptionsStep'}))}
          layout={'horizontal'}
          style={{width: 1000}}
          initialValues={configOptions}
          onFinish={updateConfigOptions}
        >
          <State/>
          <FaultTolerance/>
          <HighAvailability/>
          <Resource/>
          <Additional/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
};

export default ClusterConfigOptionsSteps;
