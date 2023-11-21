import {useIntl} from "umi";
import React from "react";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {WsDorisTemplate} from "@/services/project/typings";
import {WORKSPACE_CONF} from "@/constant";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";

const DorisTemplateForm: React.FC<ModalFormProps<WsDorisTemplate>> = ({
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
          intl.formatMessage({id: 'pages.project.doris.template'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.project.doris.template'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
            ? WsDorisTemplateService.update({...values}).then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onVisibleChange) {
                  onVisibleChange(false);
                }
              }
            })
            : WsDorisTemplateService.add({...values, projectId: projectId}).then((response) => {
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
          namespace: data.namespace,
          remark: data.remark
        }}
      >
        <ProFormDigit name={"id"} hidden/>
        <ProFormText
          name={"name"}
          label={intl.formatMessage({id: 'pages.project.doris.template.name'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"namespace"}
          label={intl.formatMessage({id: 'pages.project.doris.template.namespace'})}
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

export default DorisTemplateForm;
