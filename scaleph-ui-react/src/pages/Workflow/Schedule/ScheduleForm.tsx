import React, {useEffect, useState} from "react";
import {Form, message, Modal} from "antd";
import {ProForm, ProFormDateTimeRangePicker, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {ModalFormProps} from '@/app.d';
import {WorkflowDefinition, WorkflowSchedule} from "@/services/workflow/typings";
import CrontabSetting from "@/pages/Workflow/Schedule/CrontabSetting";
import {SchedulerService} from "@/services/workflow/scheduler.service";
import moment from 'moment';
import {TimeZoneOptions} from "@/pages/Workflow/Schedule/TimeZone";

const WorkflowScheduleForm: React.FC<ModalFormProps<{ workflow: WorkflowDefinition, schedule?: WorkflowSchedule }>> = ({
                                                                                                                         data,
                                                                                                                         visible,
                                                                                                                         onVisibleChange,
                                                                                                                         onCancel
                                                                                                                       }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  const [cronSettingData, setCronSettingData] = useState<{ visible: boolean; data: string }>({
    visible: false,
    data: "0 * * * * ?"
  });

  useEffect(() => {
    if (data.schedule) {
      const formData = {
        timezone: data.schedule.timezone,
        timeRange: [moment(data.schedule.startTime), moment(data.schedule.endTime)],
        crontab: data.schedule.crontab,
        remark: data.schedule.remark
      }
      form.setFieldsValue(formData)
    }
  }, []);

  return (
    <div>
      <Modal
        open={visible}
        title={
          data.schedule
            ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
            intl.formatMessage({id: 'pages.admin.workflow.schedule.setting'})
            : intl.formatMessage({id: 'app.common.operate.new.label'}) +
            intl.formatMessage({id: 'pages.admin.workflow.schedule.setting'})
        }
        width={580}
        destroyOnClose={true}
        onCancel={onCancel}
        onOk={() => {
          form.validateFields().then((values) => {
            const params = {
              timezone: values.timezone,
              startTime: values.timeRange[0].format('YYYY-MM-DD HH:mm:ss'),
              endTime: values.timeRange[1].format('YYYY-MM-DD HH:mm:ss'),
              crontab: values.crontab,
              remark: values.remark
            }
            data.schedule
              ? SchedulerService.update(data.schedule.id, {...params}).then((response) => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                  if (onVisibleChange) {
                    onVisibleChange(false);
                  }
                }
              })
              : SchedulerService.add({...params, workflowDefinitionId: data.workflow.id}).then((response) => {
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
        <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
          <ProFormSelect
            name={"timezone"}
            label={intl.formatMessage({id: 'pages.admin.workflow.schedule.timezone'})}
            rules={[{required: true}]}
            showSearch={true}
            allowClear={false}
            initialValue={"Asia/Shanghai"}
            options={TimeZoneOptions}
          />
          <ProFormDateTimeRangePicker
            name="timeRange"
            label={intl.formatMessage({id: 'pages.admin.workflow.schedule.timeRange'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name="crontab"
            label={intl.formatMessage({id: 'pages.admin.workflow.schedule.crontab'})}
            rules={[{required: true}]}
            fieldProps={{
              onFocus: (event) => {
                setCronSettingData({visible: true, data: form.getFieldValue("crontab")});
              }
            }}
          />
          <ProFormText
            name="remark"
            label={intl.formatMessage({id: 'pages.dataSource.remark'})}
          />
        </ProForm>
      </Modal>
      {cronSettingData.visible && (
        <CrontabSetting
          visible={cronSettingData.visible}
          onVisibleChange={(visible) => {
            setCronSettingData({visible: false, data: "0 * * * * ?"});
          }}
          onCancel={() => {
            setCronSettingData({visible: false, data: "0 * * * * ?"});
          }}
          onOK={(values) => {
            form.setFieldValue("crontab", values)
            setCronSettingData({visible: false, data: values});
          }}
          data={cronSettingData.data}
        />
      )}
    </div>
  );
}

export default WorkflowScheduleForm;
