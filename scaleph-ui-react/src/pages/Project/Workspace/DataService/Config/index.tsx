import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, NodeIndexOutlined} from "@ant-design/icons";
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {history, useAccess, useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {DataserviceConfig} from "@/services/dataservice/typings";
import {DataserviceConfigService} from "@/services/dataservice/DataserviceConfigService";

const DataServiceConfigWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DataserviceConfig[]>([]);
  const [dataserviceConfigFormData, setDataserviceConfigFormData] = useState<{
    visiable: boolean;
    data: DataserviceConfig;
  }>({visiable: false, data: {}});
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const tableColumns: ProColumns<DataserviceConfig>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.dataservice.config.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.project.dataservice.config.path'}),
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.project.dataservice.config.method'}),
      dataIndex: 'method',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.project.dataservice.config.contentType'}),
      dataIndex: 'contentType',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.project.dataservice.config.status'}),
      dataIndex: 'status',
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
                  setDataserviceConfigFormData({visiable: true, data: record});
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
            <Tooltip title={intl.formatMessage({id: 'pages.project.dataservice.config.detail'})}>
              <Button
                shape="default"
                type="link"
                icon={<NodeIndexOutlined/>}
                onClick={() => {
                  // history.push("/workspace/doris/template/detail", record)
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
                      DataserviceConfigService.deleteOne(record).then((d) => {
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

  return (<PageContainer title={false}>
    <ProTable<DataserviceConfig>
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
        DataserviceConfigService.list({...params, projectId: projectId})
      }
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                history.push("/workspace/data-service/config/steps")
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
                    DataserviceConfigService.deleteBatch(selectedRows).then((d) => {
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
  </PageContainer>);
}

export default DataServiceConfigWeb;
