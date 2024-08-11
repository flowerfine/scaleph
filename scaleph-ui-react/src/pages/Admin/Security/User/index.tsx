import {useAccess, useIntl} from '@umijs/max';
import React, {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tag, Tooltip,} from 'antd';
import {DeleteOutlined, EditOutlined, FormOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {SysDictService} from "@/services/admin/system/sysDict.service";
import {SecUser} from '@/services/admin/typings';
import {UserService} from '@/services/admin/security/user.service';
import UserForm from "@/pages/Admin/Security/User/components/UserForm";
import UserAssignRoleForm from "@/pages/Admin/Security/User/components/UserAssignRoleForm";

const User: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecUser[]>([]);
  const [userFormData, setUserFormData] = useState<{ visible: boolean; data: SecUser }>({
    visible: false,
    data: {},
  });
  const [userAssignRole, setUserAssignRole] = useState<{
    visiable: boolean;
    data: SecUser;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<SecUser>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.user.type'}),
      dataIndex: 'type',
      render: (dom, entity) => {
        return (<Tag>{entity.type?.label}</Tag>)
      },
      request: (params, props) => {
        return SysDictService.listDictByDefinition(DICT_TYPE.carpSecUserType)
      },
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.userName'}),
      dataIndex: 'userName',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.nickName'}),
      dataIndex: 'nickName',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.email'}),
      dataIndex: 'email',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.phone'}),
      dataIndex: 'phone',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.admin.user.status'}),
      dataIndex: 'userStatus',
      render: (text, record, index) => {
        return (<Tag>{record.status?.label}</Tag>)
      },
      request: (params, props) => {
        return SysDictService.listDictByDefinition(DICT_TYPE.carpSecUserStatus)
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 180,
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
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.roles'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FormOutlined/>}
                  onClick={() => setUserAssignRole({visiable: true, data: record})}
                />
              </Tooltip>
            )}

            {access.canAccess(PRIVILEGE_CODE.userEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  disabled={record.type.value == '0' || record.status.value == '2'}
                  onClick={() => {
                    setUserFormData({visible: true, data: record});
                  }}
                ></Button>
              </Tooltip>
            )}

            {access.canAccess(PRIVILEGE_CODE.userDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  danger
                  icon={<DeleteOutlined/>}
                  disabled={record.type.value == '0' || record.status.value == '2'}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        UserService.delete(record).then((d) => {
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
    <PageContainer title={false}>
      <ProTable<SecUser>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        scroll={{x: 800}}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.userAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setUserFormData({visible: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.userDelete) && (
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
                      UserService.deleteBatch(selectedRows).then((d) => {
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
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return UserService.listUserByPage({...params});
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
      {userFormData.visible ? (
        <UserForm
          visible={userFormData.visible}
          onCancel={() => {
            setUserFormData({visible: false, data: {}});
          }}
          onOK={(values) => {
            setUserFormData({visible: false, data: {}});
            actionRef.current?.reload();
          }}
          data={userFormData.data}
        />
      ) : null}
      {userAssignRole.visiable ? (
        <UserAssignRoleForm
          visible={userAssignRole.visiable}
          onCancel={() => {
            setUserAssignRole({visiable: false, data: {}});
          }}
          data={userAssignRole.data}
        />
      ) : null}
    </PageContainer>
  );
};

export default User;
