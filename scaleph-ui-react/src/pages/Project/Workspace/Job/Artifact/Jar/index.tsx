import { PRIVILEGE_CODE } from '@/constant';
import { FlinkArtifactJarService } from '@/services/project/flinkArtifactJar.service';
import { ProjectService } from '@/services/project/project.service';
import { WsProject, WsFlinkArtifact, WsFlinkArtifactJar } from '@/services/project/typings';
import { DeleteOutlined, DownloadOutlined, EditOutlined } from '@ant-design/icons';
import {
  ActionType,
  PageHeader,
  ProColumns,
  ProFormInstance,
  ProTable,
} from '@ant-design/pro-components';
import { Button, Descriptions, message, Modal, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl, useLocation } from 'umi';
import FlinkArtifactJarForm from './components/FlinkArtifactJarForm';

const FlinkArtifactJarWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [project, setProject] = useState<WsProject>({});
  const [flinkArtifact, setFlinkArtifact] = useState<WsFlinkArtifact>({});
  const [flinkArtifactJarData, setFlinkArtifactJarData] = useState<{
    visiable: boolean;
    data: WsFlinkArtifactJar;
  }>({ visiable: false, data: {} });

  useEffect(() => {
    const artifact = urlParams.state as WsFlinkArtifact;
    setFlinkArtifact(artifact);

    ProjectService.selectOne(artifact.projectId as number).then((d) => {
      setProject(d);
    });
  }, []);

  const tableColumns: ProColumns<WsFlinkArtifactJar>[] = [
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.jar.fileName' }),
      dataIndex: 'fileName',
      hideInSearch: true,
      width: 240,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.jar.version' }),
      dataIndex: 'version',
      hideInSearch: true,
      width: 120,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.flinkRelease.version' }),
      dataIndex: 'flinkVersion',
      hideInSearch: true,
      width: 120,
      render: (text, record, index) => {
        return record.flinkVersion?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.jar.path' }),
      dataIndex: 'path',
      width: 240,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.jar.entryClass' }),
      dataIndex: 'entryClass',
      hideInSearch: true,
      width: 240,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.jar.jarParams' }),
      dataIndex: 'jarParams',
      hideInSearch: true,
      width: 240,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.artifact.updateTime' }),
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
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setFlinkArtifactJarData({ visiable: true, data: record });
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.download.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined />}
                  onClick={() => {
                    FlinkArtifactJarService.download(record);
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
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
                        FlinkArtifactJarService.deleteOne(record).then((d) => {
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
                />
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];
  return (
    <>
      <div style={{ backgroundColor: 'white' }}>
        <PageHeader
          title={intl.formatMessage({ id: 'pages.project.artifact.jar' })}
          onBack={() => {
            window.history.back();
          }}
          extra={
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactJarData({
                    visiable: true,
                    data: { flinkArtifact: flinkArtifact },
                  });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.upload.label' })}
              </Button>
            )
          }
        >
          <Descriptions size="small" column={3} style={{ marginLeft: 48 }}>
            <Descriptions.Item label={intl.formatMessage({ id: 'pages.project' })}>
              {project.projectCode}
            </Descriptions.Item>
            <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.artifact.name' })}>
              {flinkArtifact.name}
            </Descriptions.Item>
            <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.artifact.remark' })}>
              {flinkArtifact.remark}
            </Descriptions.Item>
          </Descriptions>
        </PageHeader>
      </div>
      <ProTable<WsFlinkArtifactJar>
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        scroll={{ x: 1200, y: 480 }}
        search={false}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return FlinkArtifactJarService.list({ ...params, flinkArtifactId: flinkArtifact.id });
        }}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {flinkArtifactJarData.visiable && (
        <FlinkArtifactJarForm
          visible={flinkArtifactJarData.visiable}
          onCancel={() => {
            setFlinkArtifactJarData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactJarData({ visiable: visiable, data: { flinkArtifact: flinkArtifact } });
            actionRef.current?.reload();
          }}
          data={flinkArtifactJarData.data}
        />
      )}
    </>
  );
};

export default FlinkArtifactJarWeb;
