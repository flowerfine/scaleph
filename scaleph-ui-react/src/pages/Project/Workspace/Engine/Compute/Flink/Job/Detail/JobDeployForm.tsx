import React, {useRef} from "react";
import {message, Modal} from "antd";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {ModalFormProps} from '@/typings';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstanceDeployParam} from "@/services/project/typings";
import FlinkKubernetesJobDeployResourceStepForm
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/DeployResourceStepForm";
import FlinkKubernetesJobDeployStateStepForm from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/DeployStateStepForm";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobDeployForm: React.FC<ModalFormProps<WsFlinkKubernetesJob>> = ({
                                                                                        data,
                                                                                        visible,
                                                                                        onVisibleChange,
                                                                                        onCancel
                                                                                      }) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  return (
    <Modal
      open={visible}
      title={intl.formatMessage({id: 'app.common.operate.deploy.label'}) + ' ' + data.name}
      width={'50%'}
      footer={null}
      destroyOnClose={true}
      onCancel={onCancel}
    >
      <ProCard className={'step-form-submitter'}>
        <StepsForm
          formRef={formRef}
          formProps={{
            grid: true,
            rowProps: {gutter: [16, 8]},
            layout: "horizontal"
          }}
          onFinish={(values: Record<string, any>) => {
            const jobManagerSpec = {
              resource: {
                cpu: values.jobManagerCpu,
                memory: values.jobManagerMemory,
              },
              replicas: values.jobManagerReplicas
            }
            const taskManagerSpec = {
              resource: {
                cpu: values.taskManagerCpu,
                memory: values.taskManagerMemory,
              },
              replicas: values.taskManagerReplicas
            }
            const param: WsFlinkKubernetesJobInstanceDeployParam = {
              wsFlinkKubernetesJobId: data.id,
              jobManager: jobManagerSpec,
              taskManager: taskManagerSpec,
              parallelism: values.parallelism,
              upgradeMode: values.upgradeMode,
              allowNonRestoredState: values.allowNonRestoredState,
              userFlinkConfiguration: values.userFlinkConfiguration
            }
            return WsFlinkKubernetesJobService.deploy(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                onVisibleChange(false);
              }
            })
          }}
        >
          <StepsForm.StepForm
            name="resource"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource'})}
          >
            <FlinkKubernetesJobDeployResourceStepForm/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="state"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.state'})}
          >
            <FlinkKubernetesJobDeployStateStepForm/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>

    </Modal>
  );
}

export default FlinkKubernetesJobDeployForm;
