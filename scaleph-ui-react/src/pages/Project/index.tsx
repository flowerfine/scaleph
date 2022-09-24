import { PRIVILEGE_CODE, WORKSPACE_CONF } from '@/constant';
import { ProjectService } from '@/services/project/project.service';
import { DiProject } from '@/services/project/typings';
import { DeleteOutlined, EditOutlined, FolderOpenOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { history, useAccess, useIntl } from 'umi';
import ProjectForm from './components/ProjectForm';

const Project: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DiProject[]>([]);
  const [projectFormData, setProjectFormData] = useState<{
    visible: boolean;
    data: DiProject;
  }>({ visible: false, data: {} });

  const tableColumns: ProColumns<DiProject>[] = [
    {
      title: intl.formatMessage({ id: 'pages.project.projectCode' }),
      dataIndex: 'projectCode',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.projectName' }),
      dataIndex: 'projectName',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.remark' }),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.updateTime' }),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevJobShow) && (
              <Tooltip title={intl.formatMessage({ id: 'pages.project.open' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FolderOpenOutlined />}
                  onClick={() => {
                    localStorage.setItem(WORKSPACE_CONF.projectId, record.id + '');
                    history.push('/workspace');
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setProjectFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined />}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                      okButtonProps: { danger: true },
                      cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                      onOk() {
                        ProjectService.deleteProjectRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.delete.success' }),
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

  useEffect(() => {
    //remove projectid from local store
    localStorage.removeItem(WORKSPACE_CONF.projectId);
  }, []);

  return (
    <div>
      <ProTable<DiProject>
        headerTitle={intl.formatMessage({ id: 'pages.project' })}
        search={{
          labelWidth: 'auto',
          span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return ProjectService.listProjectByPage(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setProjectFormData({ visible: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevProjectDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                    content: intl.formatMessage({
                      id: 'app.common.operate.delete.confirm.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    okButtonProps: { danger: true },
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      ProjectService.deleteProjectBatch(selectedRows).then((d) => {
                        if (d.success) {
                          message.success(
                            intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                          );
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
              </Button>
            ),
          ],
        }}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        rowSelection={{
          fixed: true,
          onChange(selectedRowKeys, selectedRows, info) {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {projectFormData.visible && (
        <ProjectForm
          visible={projectFormData.visible}
          onCancel={() => {
            setProjectFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setProjectFormData({ visible: visible, data: {} });
            actionRef.current?.reload();
          }}
          data={projectFormData.data}
        />
      )}
    </div>
  );
};

export default Project;
