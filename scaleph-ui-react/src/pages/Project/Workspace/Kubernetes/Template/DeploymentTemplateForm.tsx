import {useIntl} from "umi";
import React from "react";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {
  WsFlinkKubernetesTemplateService
} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WORKSPACE_CONF} from "@/constant";

const DeploymentTemplateForm: React.FC<ModalFormProps<WsFlinkKubernetesTemplate>> = ({
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
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.template'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.template'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const metadataData = {
            ...data.metadata,
            name: values.name,
            namespace: values.namespace
          }
          const param: WsFlinkKubernetesTemplate = {
            id: values.id,
            projectId: projectId,
            name: values.name,
            metadata: metadataData,
            spec: data.spec ? data.spec : {},
            remark: values.remark,
          };
          data.id
            ? WsFlinkKubernetesTemplateService.update(param).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkKubernetesTemplateService.add(param).then((response) => {
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
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data.id,
          name: data.name,
          namespace: data.metadata?.namespace,
          remark: data.remark
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.template.name'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"namespace"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.template.namespace'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default DeploymentTemplateForm;
