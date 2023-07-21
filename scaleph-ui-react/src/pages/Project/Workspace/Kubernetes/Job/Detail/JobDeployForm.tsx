import {useIntl} from "umi";
import React from "react";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormGroup, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {WORKSPACE_CONF} from "@/constant";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobDeployForm: React.FC<ModalFormProps<WsFlinkKubernetesJob>> = ({
                                                                                        data,
                                                                                        visible,
                                                                                        onVisibleChange,
                                                                                        onCancel
                                                                                      }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({id: 'app.common.operate.deploy.label'}) + ' ' + data.name
      }
      width={'50%'}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
            ? WsFlinkKubernetesJobService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkKubernetesJobService.add({...values, projectId: projectId}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            });
        });
      }}
    >
      <ProForm
        form={form}
        layout={"horizontal"}
        submitter={false}
        grid={true}
        labelCol={{span: 16}}
        wrapperCol={{span: 8}}
        initialValues={{
          id: data?.id,
          name: data?.name,
          executionMode: data?.executionMode?.value,
          flinkDeploymentMode: data?.flinkDeploymentMode?.value,
          flinkDeploymentId: data?.flinkDeployment?.id,
          flinkSessionClusterId: data?.flinkSessionCluster?.id,
          type: data?.type?.value,
          flinkArtifactJarId: data?.flinkArtifactJar?.id,
          flinkArtifactSqlId: data?.flinkArtifactSql?.id,
          remark: data?.remark
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormGroup>
          <ProFormDigit
            name="jobManager.resource.cpu"
            label={'JobManager CPU'}
            colProps={{span: 10}}
            initialValue={1.0}
            fieldProps={{
              min: 0,
              precision: 2
            }}
          />
          <ProFormText
            name="jobManager.resource.memory"
            label={'JobManager Memory'}
            colProps={{span: 10}}
            initialValue={"1G"}
          />
          <ProFormDigit
            name="taskManager.resource.cpu"
            label={'TaskManager CPU'}
            colProps={{span: 10}}
            initialValue={1.0}
            fieldProps={{
              min: 0,
              precision: 2
            }}
          />

          <ProFormText
            name="taskManager.resource.memory"
            label={'TaskManager Memory'}
            colProps={{span: 10}}
            initialValue={"1G"}
          />
          <ProFormDigit
            name="jobManager.replicas"
            label={'JobManager Replicas'}
            colProps={{span: 10}}
            initialValue={1}
            fieldProps={{
              min: 1
            }}
          />
          <ProFormDigit
            name="taskManager.replicas"
            label={'TaskManager Replicas'}
            colProps={{span: 10}}
            initialValue={1}
            fieldProps={{
              min: 1
            }}
          />
        </ProFormGroup>
      </ProForm>
    </Modal>
  );
}

export default FlinkKubernetesJobDeployForm;
