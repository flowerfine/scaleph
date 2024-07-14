import React, {useRef} from 'react';
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {connect, useAccess, useIntl, useLocation} from '@umijs/max';
import {WsArtifact, WsArtifactFlinkCDC, WsArtifactFlinkCDCAddParam,} from "@/services/project/typings";
import {WORKSPACE_CONF} from "@/constants/constant";
import DataIntegrationFlinkCDCStepBase from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/BaseStepForm";
import DataIntegrationFlinkCDCStepConfig
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/ConfigStepForm";
import {WsArtifactFlinkCDCService} from "@/services/project/WsArtifactFlinkCDCService";
import DataIntegrationFlinkCDCStepYaml from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/YamlStepForm";

const DataIntegrationFlinkCDCNewSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const formRef = useRef<ProFormInstance>();
  const data = useLocation().state as WsArtifactFlinkCDC;
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    const artifact: WsArtifact = {
      projectId: localProjectId,
      name: values.name,
      remark: values.remark
    }
    const param: WsArtifactFlinkCDC = {
      artifact: artifact,
      parallelism: values.parallelism,
      localTimeZone: values.localTimeZone,
    }
    editFlinkCDCConfig(param)
    return Promise.resolve(true)
  }

  const onConfigStepFinish = (values: Record<string, any>) => {
    try {
      console.log('onConfigStepFinish', values)
      const instance: WsArtifactFlinkCDC = WsArtifactFlinkCDCService.formatData(props.flinkCDCSteps.instance, values)
      editFlinkCDCConfig(instance)
    } catch (unused) {
    }
    return Promise.resolve(true)
  }

  const editFlinkCDCConfig = (param: WsArtifactFlinkCDC) => {
    props.dispatch({
      type: 'flinkCDCSteps/editInstance',
      payload: param
    })
  }

  return (
    <PageContainer title={false}>
      <ProCard className={'step-form-submitter'}>
        <StepsForm
          formRef={formRef}
          formProps={{
            grid: true,
            rowProps: {gutter: [16, 8]}
          }}
          onFinish={(values: Record<string, any>) => {
            const param: WsArtifactFlinkCDCAddParam = {
              projectId: localProjectId,
              name: values.name,
              parallelism: values.parallelism,
              localTimeZone: values.localTimeZone,
              remark: values.remark,
              fromDsId: values.fromDsId,
              toDsId: values.toDsId,
              transform: values.transform,
              route: values.route
            }
            return WsArtifactFlinkCDCService.add(param).then(response => response.success);
          }}
        >
          <StepsForm.StepForm
            name="base"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.base'})}
            style={{width: 1000}}
            onFinish={onBaseStepFinish}>
            <DataIntegrationFlinkCDCStepBase/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="config"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config'})}
            style={{width: 1000}}
            onFinish={onConfigStepFinish}>
            <DataIntegrationFlinkCDCStepConfig/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="yaml"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.yaml'})}
            style={{width: 1100}}>
            <DataIntegrationFlinkCDCStepYaml/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  );
};

const mapModelToProps = ({flinkCDCSteps}: any) => ({flinkCDCSteps})
export default connect(mapModelToProps)(DataIntegrationFlinkCDCNewSteps);
