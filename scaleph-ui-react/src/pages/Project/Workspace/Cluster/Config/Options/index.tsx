import {useRef} from 'react';
import {history, useIntl, useLocation} from 'umi';
import {message} from 'antd';
import {ProCard, ProFormInstance, StepsForm} from '@ant-design/pro-components';
import {WORKSPACE_CONF} from '@/constant';
import BaseOptions from './components/Base';
import State from './components/State';
import FaultTolerance from './components/FaultTolerance';
import HighAvailability from './components/HA';
import Resource from './components/Resource';
import Additional from './components/Additional';
import {WsFlinkClusterConfig} from '@/services/project/typings';
import {FlinkClusterConfigService} from '@/services/project/flinkClusterConfig.service';

const ClusterConfigOptionsSteps: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const params = urlParams.state as WsFlinkClusterConfig;

  const configOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(params?.configOptions ? params?.configOptions : {})),
  );

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={async (values) => {
          let cluster: WsFlinkClusterConfig = {
            projectId: values.projectId,
            name: values.name,
            clusterCredential: {id: values.clusterCredentialId},
            deployMode: values.deployMode,
            flinkRelease: {id: values.flinkReleaseId},
            resourceProvider: values.resourceProvider,
            configOptions: FlinkClusterConfigService.getData(values),
            remark: values.remark,
          };
          params.id
            ? FlinkClusterConfigService.update({...cluster, id: params.id}).then((d) => {
              message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
              history.back();
            })
            : FlinkClusterConfigService.add(cluster).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                history.back();
              }
            });
        }}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({id: 'page.project.cluster.config.baseStep'})}
          style={{width: 600}}
          initialValues={{
            projectId: projectId,
            name: params?.name,
            resourceProvider: params?.resourceProvider?.value,
            deployMode: params?.deployMode?.value,
            clusterCredentialId: params?.clusterCredential?.id,
            flinkVersion: params?.flinkVersion?.value,
            flinkReleaseId: params?.flinkRelease?.id,
            remark: params?.remark,
          }}
        >
          <BaseOptions/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="configOptions"
          title={intl.formatMessage({id: 'page.project.cluster.config.configOptionsStep'})}
          style={{width: 700}}
          initialValues={configOptions}
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
