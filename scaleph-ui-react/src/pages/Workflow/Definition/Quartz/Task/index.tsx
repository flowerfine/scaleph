import {history, useAccess, useIntl, useLocation} from "umi";
import {useRef} from "react";
import {Button, Space, Tooltip} from "antd";
import {FolderOpenOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {WorkflowDefinition, WorkflowTaskDefinition} from "@/services/workflow/typings";
import {WorkflowService} from "@/services/workflow/workflow.service";
import {PRIVILEGE_CODE} from "@/constant";

const QuartzTaskDefinition: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const workflowDefinition = urlParams.state as WorkflowDefinition;

  const tableColumns: ProColumns<WorkflowTaskDefinition>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.task.type'}),
      dataIndex: 'type',
      render: (dom, entity, index, action, schema) => {
        return entity.type.label
      },
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.task.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.admin.workflow.quartz.task.handler'}),
      dataIndex: 'handler'
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.remark'}),
      dataIndex: 'remark',
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
        </Space>
      ),
    },
  ]

  return (
    <ProTable<WorkflowTaskDefinition>
      search={false}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        return WorkflowService.listWorkflowTaskDefinitions(workflowDefinition.id);
      }}
      pagination={false}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
}

export default QuartzTaskDefinition;
