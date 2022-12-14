import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {history, useAccess, useIntl, useLocation} from "umi";
import {WorkflowDefinition, WorkflowSchedule} from "@/services/workflow/typings";
import {PRIVILEGE_CODE} from "@/constant";
import {SchedulerService} from "@/services/workflow/scheduler.service";
import ScheduleForm from "@/pages/Workflow/Schedule/ScheduleForm";

const WorkflowScheduleWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<WorkflowSchedule[]>([]);
  const [scheduleFormData, setScheduleFormData] = useState<{ visible: boolean; data: { workflow: WorkflowDefinition, schedule?: WorkflowSchedule } }>({
    visible: false,
    data: {}
  });

  const workflowDefinition = urlParams.state as WorkflowDefinition;

  const tableColumns: ProColumns<WorkflowSchedule>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.status'}),
      dataIndex: 'status',
      render: (dom, entity, index, action, schema) => {
        return entity.status.label
      },
      width: 80
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.timezone'}),
      dataIndex: 'timezone',
      width: 85
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.startTime'}),
      dataIndex: 'startTime',
      width: 180
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.endTime'}),
      dataIndex: 'endTime',
      width: 180
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.crontab'}),
      dataIndex: 'crontab',
      width: 120
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.remark'}),
      dataIndex: 'remark'
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.createTime'}),
      dataIndex: 'createTime',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.updateTime'}),
      dataIndex: 'updateTime',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <Space>
          {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
              <Button
                shape="default"
                type="link"
                icon={<EditOutlined/>}
                disabled={record.status.value == '1'}
                onClick={() => {
                  setScheduleFormData({
                    visible: true,
                    data: {workflow: workflowDefinition, schedule: record}
                  })
                }}
              ></Button>
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
              <Button
                shape="default"
                type="link"
                icon={<DeleteOutlined/>}
                disabled={record.status.value == '1'}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      SchedulerService.deleteOne(record).then((response) => {
                        if (response.success) {
                          message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              ></Button>
            </Tooltip>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <ProTable<WorkflowSchedule>
        headerTitle={
          <Button key="return" type="default" onClick={() => history.back()}>
            {intl.formatMessage({id: 'app.common.operate.return.label'})}
          </Button>
        }
        search={false}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return SchedulerService.list({workflowDefinitionId: workflowDefinition.id});
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setScheduleFormData({visible: true, data: {workflow: workflowDefinition}})}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
                onClick={() => {
                  if (selectedRows.filter((row) => row.status.value = '1').length > 0) {
                    message.error(intl.formatMessage({id: 'app.common.operate.delete.disabled'}));
                    return;
                  }
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      SchedulerService.deleteBatch(selectedRows).then((response) => {
                        if (response.success) {
                          message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.delete.label'})}
              </Button>
            ),
          ],
        }}
        pagination={false}
        rowSelection={{
          fixed: true,
          onChange(selectedRowKeys, selectedRows, info) {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {scheduleFormData.visible && (
        <ScheduleForm
          visible={scheduleFormData.visible}
          onCancel={() => {
            setScheduleFormData({visible: false, data: {}});
          }}
          onVisibleChange={(visible) => {
            setScheduleFormData({visible: false, data: {}});
            actionRef.current?.reload();
          }}
          data={scheduleFormData.data}
        />
      )}
    </div>
  );
}

export default WorkflowScheduleWeb;
