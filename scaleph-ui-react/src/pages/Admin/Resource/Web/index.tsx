import {useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, PlusOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {isEmpty} from "lodash";
import {PRIVILEGE_CODE} from "@/constant";
import {SecResourceWeb} from "@/services/admin/typings";
import {PrivilegeService} from "@/services/admin/privilege.service";
import WebResourceForm from "@/pages/Admin/Resource/Web/components/WebResourceForm";
import {ResourceWebService} from "@/services/admin/resourceWeb.service";

const WebResourceWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecResourceWeb[]>([]);
  const [webResourceFormData, setWebResourceFormData] = useState<{
    visiable: boolean;
    parent: SecResourceWeb;
    data: SecResourceWeb;
  }>({visiable: false, parent: {}, data: {}});

  const onExpand = (expanded: boolean, record: SecResourceWeb) => {
    if (expanded && record.children && isEmpty(record.children)) {
      PrivilegeService.listByPid(record.id).then((response) => {
        record.children = response.data
      })
    }
  }

  const tableColumns: ProColumns<SecResourceWeb>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.resource.type'}),
      dataIndex: 'type',
      render: (dom, entity) => {
        return (<Tag>{entity.type?.label}</Tag>)
      },
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.path'}),
      dataIndex: 'path',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.redirect'}),
      dataIndex: 'redirect',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.layout'}),
      dataIndex: 'layout',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.icon'}),
      dataIndex: 'icon',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.component'}),
      dataIndex: 'component',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true
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
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<PlusOutlined/>}
                  onClick={() => setWebResourceFormData({visiable: true, parent: record, data: {}})}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => setWebResourceFormData({visiable: true, parent: {}, data: record})}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        ResourceWebService.deleteOne(record).then((d) => {
                          if (d.success) {
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
        </>
      ),
    },
  ];

  return (
    <div>
      <ProTable<SecResourceWeb>
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
          return ResourceWebService.listByPage({...params, pid: 0})
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setWebResourceFormData({visiable: true, parent: null, data: {}})}
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
                      ResourceWebService.deleteBatch(selectedRows).then((d) => {
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
            ),
          ],
        }}
        pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
        rowSelection={{
          fixed: true,
          onChange: (selectedRowKeys, selectedRows, info) => setSelectedRows(selectedRows),
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {webResourceFormData.visiable && (
        <WebResourceForm
          visible={webResourceFormData.visiable}
          onCancel={() => setWebResourceFormData({visiable: false, parent: {}, data: {}})}
          onVisibleChange={(visiable) => {
            setWebResourceFormData({visiable: visiable, parent: {}, data: {}});
            actionRef.current?.reload();
          }}
          parent={webResourceFormData.parent}
          data={webResourceFormData.data}
        />
      )}
    </div>
  );

}

export default WebResourceWeb;
