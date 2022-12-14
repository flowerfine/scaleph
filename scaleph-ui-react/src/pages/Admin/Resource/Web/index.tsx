import {useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, PlusOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {isEmpty} from "lodash";
import {PRIVILEGE_CODE} from "@/constant";
import {SecPrivilege} from "@/services/admin/typings";
import {PrivilegeService} from "@/services/admin/privilege.service";
import WebResourceForm from "@/pages/Admin/Resource/Web/components/WebResourceForm";

const WebResourceWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecPrivilege[]>([]);
  const [webResourceFormData, setWebResourceFormData] = useState<{
    visiable: boolean;
    parent: SecPrivilege;
    data: SecPrivilege;
  }>({visiable: false, parent: {}, data: {}});

  const onExpand = (expanded: boolean, record: SecPrivilege) => {
    if (expanded && record.children && isEmpty(record.children)) {
      PrivilegeService.listByPid(record.id).then((response) => {
        record.children = response.data
      })
    }
  }

  const tableColumns: ProColumns<SecPrivilege>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.resource.privilegeName'}),
      dataIndex: 'privilegeName',
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.privilegeCode'}),
      dataIndex: 'privilegeCode',
      hideInSearch: true,
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.resourceType'}),
      dataIndex: 'resourceType',
      render: (dom, entity) => {
        return (<Tag>{entity.resourceType?.label}</Tag>)
      },
      hideInSearch: true,
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.resourcePath'}),
      dataIndex: 'resourcePath',
      hideInSearch: true,
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.updateTime'}),
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
                ></Button>
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
                        PrivilegeService.deleteOne(record).then((d) => {
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
      <ProTable<SecPrivilege>
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
          return PrivilegeService.listByPage({...params, pid: 0})
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
                      PrivilegeService.deleteBatch(selectedRows).then((d) => {
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
