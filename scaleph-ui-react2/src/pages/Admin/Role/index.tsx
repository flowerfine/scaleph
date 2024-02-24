import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {DICT_TYPE} from '@/constants/dictType';
import RoleForm from '@/pages/Admin/Role/components/RoleForm';
import {DictDataService} from '@/services/admin/dictData.service';
import {RoleService} from '@/services/admin/role.service';
import {SecRole} from '@/services/admin/typings';
import {DeleteOutlined, EditOutlined, FormOutlined, SelectOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProFormSelect, ProTable,} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tag, Tooltip} from 'antd';
import React, {useRef, useState} from 'react';
import {useAccess, useIntl} from 'umi';
import WebAssugnRoles from './components/WebAssugnRoles';
import ResourceWebs from './components/ResourceWebs';

const RoleWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecRole[]>([]);
  const [roleFormData, setRoleFormData] = useState<{
    visiable: boolean;
    data: SecRole;
  }>({visiable: false, data: {}});

  const [webAssignRoles, setWebAssignRoles] = useState<{
    visiable: boolean;
    data: SecRole;
  }>({visiable: false, parent: {}, data: {}});

  const [resourceWebs, setResourceWebs] = useState<{
    visiable: boolean;
    data: SecRole;
  }>({visiable: false, parent: {}, data: {}});

  const tableColumns: ProColumns<SecRole>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.role.name'}),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.role.code'}),
      dataIndex: 'code',
      hideInSearch: true,
      width: 200,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.role.type'}),
      dataIndex: 'type',
      render: (dom, entity) => {
        return <Tag>{entity.type?.label}</Tag>;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleType)}
          />
        );
      },
      width: 200,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.role.status'}),
      dataIndex: 'status',
      render: (dom, entity) => {
        return <Tag>{entity.status?.label}</Tag>;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleStatus)}
          />
        );
      },
      width: 200,
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
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.user'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FormOutlined/>}
                  onClick={() => setWebAssignRoles({visiable: true, data: record})}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.webs'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<SelectOutlined/>}
                  onClick={() => setResourceWebs({visiable: true, data: record})}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.roleEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  disabled={record.type.value == '01'}
                  onClick={() => setRoleFormData({visiable: true, data: record})}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.roleDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.title',
                      }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        RoleService.deleteRole(record).then((d) => {
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
    <div>
      <ProTable<SecRole>
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
          return RoleService.listByPage({...params});
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.roleAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setRoleFormData({visiable: true, data: {}})}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.roleDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
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
                      RoleService.deleteBatch(selectedRows).then((d) => {
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
      {roleFormData.visiable && (
        <RoleForm
          visible={roleFormData.visiable}
          onCancel={() => setRoleFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setRoleFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={roleFormData.data}
        />
      )}
      {webAssignRoles.visiable && (
        <WebAssugnRoles
          visible={webAssignRoles.visiable}
          onCancel={() => setWebAssignRoles({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setWebAssignRoles({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={webAssignRoles.data}
        />
      )}
      {resourceWebs.visiable && (
        <ResourceWebs
          visible={resourceWebs.visiable}
          onCancel={() => setResourceWebs({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setResourceWebs({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={resourceWebs.data}
        />
      )}
    </div>
  );
};

export default RoleWeb;
