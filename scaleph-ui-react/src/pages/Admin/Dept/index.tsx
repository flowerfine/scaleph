import {useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {SecDeptTree} from "@/services/admin/typings";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {DeleteOutlined, EditOutlined, PlusOutlined} from "@ant-design/icons";
import {DeptService} from "@/services/admin/dept.service";
import DeptForm from "@/pages/Admin/Dept/components/DeptForm";

const DeptWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<SecDeptTree[]>([]);
  const [deptFormData, setDeptFormData] = useState<{
    visiable: boolean;
    parent: SecDeptTree;
    data: SecDeptTree;
  }>({visiable: false, parent: {}, data: {}});

  const tableColumns: ProColumns<SecDeptTree>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.dept.deptName'}),
      dataIndex: 'deptName',
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.admin.dept.deptCode'}),
      dataIndex: 'deptCode',
      width: 200
    },
    {
      title: intl.formatMessage({id: 'pages.admin.dept.deptStatus'}),
      dataIndex: 'deptStatus',
      render: (dom, entity) => {
        return (<Tag>{entity.deptStatus?.label}</Tag>)
      },
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
            {access.canAccess(PRIVILEGE_CODE.deptAdd) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.new.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<PlusOutlined/>}
                  onClick={() => setDeptFormData({visiable: true, parent: record, data: {}})}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.deptEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => setDeptFormData({visiable: true, parent: {}, data: record})}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.deptDelete) && (
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
                        DeptService.deleteDept(record).then((d) => {
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
      <ProTable<SecDeptTree>
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
          return DeptService.listByPage({...params, pid: 0})
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.deptAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setDeptFormData({visiable: true, parent: null, data: {}})}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.deptDelete) && (
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
                      DeptService.deleteBatch(selectedRows).then((d) => {
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
      {deptFormData.visiable && (
        <DeptForm
          visible={deptFormData.visiable}
          onCancel={() => setDeptFormData({visiable: false, parent: {}, data: {}})}
          onVisibleChange={(visiable) => {
            setDeptFormData({visiable: visiable, parent: {}, data: {}});
            actionRef.current?.reload();
          }}
          parent={deptFormData.parent}
          data={deptFormData.data}
        />
      )}
    </div>
  );
}

export default DeptWeb;
