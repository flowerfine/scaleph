import { Dict } from '@/app.d';
import { PRIVILEGE_CODE, USER_AUTH } from '@/constant';
import { listAllProject } from '@/services/project/project.service';
import {
  deleteResourceBatch,
  deleteResourceRow,
  listResourceFileByPage,
} from '@/services/resource/resource.service';
import { DiResourceFile } from '@/services/resource/typings';
import { DeleteOutlined, DownloadOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Select, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import ResourceForm from './components/ResourceForm';

const Resource: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DiResourceFile[]>([]);
  const [projectList, setProjectList] = useState<Dict[]>([]);
  const [resourceFormData, setResourceFormData] = useState<{
    visible: boolean;
    data: DiResourceFile;
  }>({ visible: false, data: {} });

  const tableColumns: ProColumns<DiResourceFile>[] = [
    {
      title: intl.formatMessage({ id: 'pages.resource.projectCode' }),
      dataIndex: 'projectCode',
      width: 180,
      renderFormItem: (item, { defaultRender, ...rest }, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {projectList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.fileName' }),
      dataIndex: 'fileName',
      width: 280,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.fileType' }),
      dataIndex: 'fileType',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.filePath' }),
      dataIndex: 'filePath',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.fileSize' }),
      dataIndex: 'fileSize',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.updateTime' }),
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
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.download.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined></DownloadOutlined>}
                  onClick={() => {
                    let url: string =
                      'api/datadev/resource/download?projectId=' +
                      record.projectId +
                      '&fileName=' +
                      record.fileName +
                      '&' +
                      USER_AUTH.token +
                      '=' +
                      localStorage.getItem(USER_AUTH.token);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = record.fileName + '';
                    a.click();
                    window.URL.revokeObjectURL(url);
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
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
                        deleteResourceRow(record).then((d) => {
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
    listAllProject().then((d) => {
      setProjectList(d);
    });
  }, []);

  return (
    <div>
      <ProTable<DiResourceFile>
        headerTitle={intl.formatMessage({ id: 'pages.resource' })}
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
          return listResourceFileByPage(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setResourceFormData({ visible: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
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
                      deleteResourceBatch(selectedRows).then((d) => {
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
      {resourceFormData.visible && (
        <ResourceForm
          visible={resourceFormData.visible}
          onCancel={() => {
            setResourceFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setResourceFormData({ visible: visible, data: {} });
            actionRef.current?.reload();
          }}
          data={resourceFormData.data}
        />
      )}
    </div>
  );
};

export default Resource;
