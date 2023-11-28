import {history, useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, NodeIndexOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE, WORKSPACE_CONF} from "@/constant";
import {WsDorisInstance, WsDorisTemplate} from "@/services/project/typings";
import {WsDorisInstanceService} from "@/services/project/WsDorisInstanceService";

const DorisInstanceWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<WsDorisInstance[]>([]);
  const [dorisInstanceFormData, setDorisInstanceFormData] = useState<{
    visiable: WsDorisInstance;
    data: WsDorisTemplate;
  }>({visiable: false, data: {}});
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const tableColumns: ProColumns<WsDorisInstance>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.doris.template.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.project.doris.template.namespace'}),
      dataIndex: 'namespace',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
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
                onClick={() => {
                  setDorisInstanceFormData({visiable: true, data: record});
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
            <Tooltip title={intl.formatMessage({id: 'pages.project.doris.template.define'})}>
              <Button
                shape="default"
                type="link"
                icon={<NodeIndexOutlined/>}
                onClick={() => {
                  history.push("/workspace/doris/template/detail", record)
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
              <Button
                shape="default"
                type="link"
                danger
                icon={<DeleteOutlined/>}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      WsDorisInstanceService.delete(record).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              />
            </Tooltip>
          )}
        </Space>
      ),
    },
  ];

  return (<div>
    <ProTable<WsDorisInstance>
      search={{
        labelWidth: 'auto',
        span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
      }}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) =>
        WsDorisInstanceService.list({...params, projectId: projectId})
      }
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                history.push("/workspace/doris/template/steps")
              }}
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
                Modal.confirm({
                  title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                  content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                  okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                  okButtonProps: {danger: true},
                  cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                  onOk() {
                    WsDorisInstanceService.deleteBatch(selectedRows).then((d) => {
                      if (d.success) {
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
          )
        ],
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      rowSelection={{
        fixed: true,
        onChange(selectedRowKeys, selectedRows, info) {
          setSelectedRows(selectedRows);
        },
      }}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  </div>);
}

export default DorisInstanceWeb;
