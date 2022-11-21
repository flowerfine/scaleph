import React from "react";
import {Button, Form, message, Modal} from "antd";
import {ProForm, ProFormSelect} from "@ant-design/pro-components";
import {history, useIntl} from "umi";
import {ModalFormProps} from '@/app.d';
import {WorkflowDefinition} from "@/services/workflow/typings";
import {SchedulerService} from "@/services/workflow/scheduler.service";

const ScheduleEnableForm: React.FC<ModalFormProps<WorkflowDefinition>> = ({
                                                                            data,
                                                                            visible,
                                                                            onVisibleChange,
                                                                            onCancel,
                                                                          }) => {

  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={intl.formatMessage({id: 'pages.admin.workflow.quartz.schedule.enable'})}
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          SchedulerService.enable(values.schedule).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.success'}));
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            }
          })
        })
      }}
    >
      <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
        <ProFormSelect
          name={"schedule"}
          label={intl.formatMessage({id: 'pages.admin.workflow.schedule.setting'})}
          rules={[{required: true}]}
          addonAfter={
            <Button type={"link"} onClick={() => history.push("/admin/workflow/schedule", data)}>
              {intl.formatMessage({id: 'app.common.operate.new.directive'})}
            </Button>}
          showSearch={true}
          allowClear={false}
          request={(params, props) => {
            return SchedulerService.list({workflowDefinitionId: data.id, status: '0'}).then((response) => {
              if (response.success && response.data) {
                return response.data.map((item) => {
                  return {value: item.id, label: item.crontab, item: item}
                })
              }
              return []
            })
          }}
        />
      </ProForm>
    </Modal>
  );
}

export default ScheduleEnableForm;
