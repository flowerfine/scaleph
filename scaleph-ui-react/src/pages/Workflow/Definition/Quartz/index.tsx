import {history, useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, Space, Switch, Tooltip} from "antd";
import {FolderOpenOutlined, SettingOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {WorkflowDefinition} from "@/services/workflow/typings";
import {WorkflowService} from "@/services/workflow/workflow.service";
import {PRIVILEGE_CODE} from "@/constant";
import ScheduleEnableForm from "@/pages/Workflow/Schedule/ScheduleEnableForm";
import {SchedulerService} from "@/services/workflow/scheduler.service";

const QuartzWorkflowDefinition: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const [scheduleEnableFormData, setScheduleEnableFormData] = useState<{ visible: boolean; data: WorkflowDefinition }>({
    visible: false,
    data: {}
  });

  const scheduleChange = (checked: boolean, record: WorkflowDefinition) => {
    if (checked) {
      setScheduleEnableFormData({visible: true, data: record})
    } else {
      if (record.schedule?.id) {
        SchedulerService.disable(record.schedule?.id).then((response) => {
          actionRef.current?.reload()
        })
      }
    }
  }

  const tableColumns: ProColumns<WorkflowDefinition>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.type'}),
      dataIndex: 'type',
      render: (dom, entity, index, action, schema) => {
        return entity.type.label
      },
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.executeType'}),
      dataIndex: 'executeType',
      render: (dom, entity, index, action, schema) => {
        return entity.executeType.label
      },
      hideInSearch: true,
      width: 120
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.schedule.crontab'}),
      dataIndex: 'schedule',
      render: (dom, entity, index, action, schema) => {
        return entity.schedule?.crontab
      },
      hideInSearch: true,
      width: 120
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.remark'}),
      dataIndex: 'remark',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.schedule'}),
      dataIndex: 'scheduleOptions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <Switch checkedChildren={"启动"} unCheckedChildren={"禁止"} checked={record.schedule != null} onChange={(checked) => scheduleChange(checked, record)}/>
      ),
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
          {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
            <Tooltip title={intl.formatMessage({id: 'pages.admin.workflow.quartz.task.open'})}>
              <Button
                shape="default"
                type="link"
                icon={<FolderOpenOutlined/>}
                onClick={() => {
                  history.push('/admin/workflow/quartz/task', record);
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
            <Tooltip title={intl.formatMessage({id: 'pages.admin.workflow.schedule.setting'})}>
              <Button
                shape="default"
                type="link"
                icon={<SettingOutlined/>}
                onClick={() => {
                  history.push('/admin/workflow/schedule', record);
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
      <ProTable<WorkflowDefinition>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return WorkflowService.listWorkflowDefinitions({...params, type: "0"});
        }}
        pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {scheduleEnableFormData.visible && (
        <ScheduleEnableForm
          visible={scheduleEnableFormData.visible}
          onCancel={() => {
            setScheduleEnableFormData({visible: false, data: {}});
          }}
          onVisibleChange={(visible) => {
            setScheduleEnableFormData({visible: false, data: {}});
            actionRef.current?.reload();
          }}
          data={scheduleEnableFormData.data}
        />
      )}
    </div>
  );
}

export default QuartzWorkflowDefinition;
