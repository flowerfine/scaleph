import {useIntl} from "umi";
import React from "react";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormRadio, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {DICT_TYPE, WORKSPACE_CONF} from "@/constant";
import {DictDataService} from "@/services/admin/dictData.service";

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
          intl.formatMessage({id: 'pages.project.flink.kubernetes.template'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.template'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
            ? WsFlinkKubernetesTemplateService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsFlinkKubernetesTemplateService.add({...values, projectId: projectId}).then((response) => {
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
          deploymentKind: data.deploymentKind?.value,
          namespace: data.namespace,
          remark: data.remark
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.name'})}
          rules={[{required: true}]}
        />
        <ProFormRadio.Group
          name={"deploymentKind"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.deploymentKind'})}
          rules={[{required: true}]}
          disabled={data?.id}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.deploymentKind)}
        />
        <ProFormText
          name={"namespace"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.namespace'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default DeploymentTemplateForm;
