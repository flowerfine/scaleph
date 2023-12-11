import {useIntl} from "umi";
import React from "react";
import {Form, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormRadio, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";

const DeploymentForm: React.FC<ModalFormProps<WsFlinkKubernetesDeployment>> = ({
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
          intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          onVisibleChange(false);
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
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.name'})}
          rules={[{required: true}]}
        />
        <ProFormRadio.Group
          name={"type"}
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.type'})}
          rules={[{required: true}]}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkJobType)}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
}

export default DeploymentForm;
