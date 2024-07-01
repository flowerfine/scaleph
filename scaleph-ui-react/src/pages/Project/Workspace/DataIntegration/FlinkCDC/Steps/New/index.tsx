import React, {useRef} from 'react';
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {useAccess, useIntl, useLocation} from '@umijs/max';
import {WsArtifactFlinkCDC, WsArtifactFlinkCDCAddParam} from "@/services/project/typings";
import {WORKSPACE_CONF} from "@/constants/constant";
import DataIntegrationFlinkCDCStepBase from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/BaseStepForm";
import DataIntegrationFlinkCDCStepConfig
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/New/ConfigStepForm";
import {WsArtifactFlinkCDCService} from "@/services/project/WsArtifactFlinkCDCService";

const DataIntegrationFlinkCDCNewSteps: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const formRef = useRef<ProFormInstance>();
  const data = useLocation().state as WsArtifactFlinkCDC;
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

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
              projectId: projectId,
              name: values.name,
              parallelism: values.parallelism,
              localTimeZone: values.localTimeZone,
              remark: values.remark,
              fromDsId: values.fromDsId,
              toDsId: values.toDsId,
              transform: values.transform,
              route: values.route
            }
            return WsArtifactFlinkCDCService.add(param);
          }}
        >
          <StepsForm.StepForm
            name="base"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.base'})}
            style={{width: 1000}}>
            <DataIntegrationFlinkCDCStepBase/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="config"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.config'})}
            style={{width: 1000}}>
            <DataIntegrationFlinkCDCStepConfig/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="yaml"
            title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.yaml'})}
            style={{width: 1100}}>

          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  );
};

export default DataIntegrationFlinkCDCNewSteps;
