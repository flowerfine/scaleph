import React from "react";
import {Form} from 'antd';
import {ModalForm, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {WORKSPACE_CONF} from "@/constants/constant";
import {WsArtifactSeaTunnel, WsArtifactSeaTunnelSaveParam} from '@/services/project/typings';
import {ModalFormProps} from "@/typings";
import {WsArtifactSeaTunnelService} from "@/services/project/WsArtifactSeaTunnelService";

const DataIntegrationSeaTunnelForm: React.FC<ModalFormProps<WsArtifactSeaTunnel>> = ({
                                                                                data,
                                                                                visible,
                                                                                onVisibleChange,
                                                                                onOK
                                                                              }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ModalForm
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.project.artifact.seatunnel'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.artifact.seatunnel'})
      }
      form={form}
      initialValues={{
        id: data?.id,
        name: data?.artifact?.name,
        remark: data?.artifact?.remark,
      }}
      open={visible}
      onOpenChange={onVisibleChange}
      width={580}
      layout={"horizontal"}
      labelCol={{span: 6}}
      wrapperCol={{span: 16}}
      modalProps={{
        destroyOnClose: true,
        closeIcon: false
      }}
      onFinish={(values: Record<string, any>) => {
        const job: WsArtifactSeaTunnelSaveParam = {
          id: values.id,
          projectId: projectId,
          name: values.name,
          remark: values.remark,
        };
        console.log('values',job, values)
        return job.id
          ? WsArtifactSeaTunnelService.update(job).then((response) => {
            if (response.success) {
              onVisibleChange(false)
            }
          })
          : WsArtifactSeaTunnelService.add(job).then((response) => {
            if (response.success) {
              onVisibleChange(false)
              onOK(response.data);
            }
          });
      }}
    >
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.artifact.name'})}
        rules={[{required: true}, {max: 200}]}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
        rules={[{max: 200}]}
      />
    </ModalForm>
  );
};

export default DataIntegrationSeaTunnelForm;
