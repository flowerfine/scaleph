import React, {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tag, Tooltip} from 'antd';
import {DeleteOutlined, EditOutlined, FormOutlined, PlusOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {useAccess, useIntl} from '@umijs/max';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import WebResourceForm from '@/pages/Admin/Security/Resource/Web/components/WebResourceForm';
import {PrivilegeService} from '@/services/admin/privilege.service';
import {ResourceWebService} from '@/services/admin/security/resourceWeb.service';
import {SecResourceWeb} from '@/services/admin/typings';
import {isEmpty} from 'lodash';
import ResourceWebAssignRoleForm from "@/pages/Admin/Security/Resource/Web/components/ResourceWebAssignRoleForm";

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
  const [webAssignRoles, setWebAssignRoles] = useState<{
    visiable: boolean;
    data: SecResourceWeb;
  }>({visiable: false, parent: {}, data: {}});

  const onExpand = (expanded: boolean, record: SecResourceWeb) => {
    if (expanded && record.children && isEmpty(record.children)) {
      PrivilegeService.listByPid(record.id).then((response) => {
        record.children = response.data;
      });
    }
  };

  const tableColumns: ProColumns<SecResourceWeb>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.resource.type'}),
      dataIndex: 'type',
      render: (dom, entity) => {
        return <Tag>{entity.type?.label}</Tag>;
      },
      fixed: 'left',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.value'}),
      dataIndex: 'value',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.label'}),
      dataIndex: 'label',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.path'}),
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.web.order'}),
      dataIndex: 'order',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.resource.status'}),
      dataIndex: 'status',
      width: 120,
      render: (dom, entity) => {
        return <Tag>{entity.status?.label}</Tag>;
      },
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 160,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.roles'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FormOutlined/>}
                  onClick={() => setWebAssignRoles({visiable: true, data: record})}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<PlusOutlined/>}
                  onClick={() =>
                    setWebResourceFormData({visiable: true, parent: record, data: {}})
                  }
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() =>
                    setWebResourceFormData({visiable: true, parent: {}, data: record})
                  }
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  danger
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        ResourceWebService.deleteOne(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({id: 'app.common.operate.delete.success'}),
                            );
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
    <PageContainer title={false}>
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
          return ResourceWebService.listByPage({...params, pid: 0});
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
                danger
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
        scroll={{x: 1500, y: 'calc( 100vh - 385px )'}}
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

      {webAssignRoles.visiable && (
        <ResourceWebAssignRoleForm
          visible={webAssignRoles.visiable}
          onCancel={() => setWebAssignRoles({visiable: false, data: {}})}
          onOK={(values) => {
            setWebAssignRoles({visiable: false, data: {}});
            actionRef.current?.reload();
          }}
          data={webAssignRoles.data}
        />
      )}
    </PageContainer>
  );
};

export default WebResourceWeb;
