import {PRIVILEGE_CODE} from '@/constant';
import {DeleteOutlined, DownloadOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {useRef, useState} from 'react';
import {useAccess, useIntl} from 'umi';
import FlinkArtifactForm from "@/pages/DEV/Artifact/components/FlinkArtifactForm";
import {FlinkArtifact} from "@/services/dev/typings";
import {deleteBatch, deleteOne, download, list} from "@/services/dev/flinkArtifact.service";

const FlinkArtifactJar: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkArtifact[]>([]);
  const [flinkArtifactFormData, setFlinkArtifactData] = useState<{
    visiable: boolean;
    data: FlinkArtifact;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<FlinkArtifact>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.entryClass'}),
      dataIndex: 'entryClass',
      hideInSearch: true,
      width: 280,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact.path'}),
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.updateTime'}),
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
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined></DownloadOutlined>}
                  onClick={() => {
                    download(record)
                  }}
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
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        deleteOne(record).then((d) => {
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
      <ProTable<FlinkArtifact>
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
          return list(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.upload.label'})}
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
                    content: intl.formatMessage({
                      id: 'app.common.operate.delete.confirm.content',
                    }),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      deleteBatch(selectedRows).then((d) => {
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
          onChange(selectedRowKeys, selectedRows, info) {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {flinkArtifactFormData.visiable && (
        <FlinkArtifactForm
          visible={flinkArtifactFormData.visiable}
          onCancel={() => {
            setFlinkArtifactData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={flinkArtifactFormData.data}
        />
      )}
    </div>
  );
};

export default FlinkArtifactJar;
