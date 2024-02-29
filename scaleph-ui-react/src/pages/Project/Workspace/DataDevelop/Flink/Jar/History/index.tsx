import {useEffect, useRef, useState} from 'react';
import {Button, Descriptions, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, DownloadOutlined} from '@ant-design/icons';
import {ActionType, PageHeader, ProColumns, ProFormInstance, ProTable,} from '@ant-design/pro-components';
import {history, useAccess, useIntl, useLocation} from '@umijs/max';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {WsArtifactFlinkJarService} from '@/services/project/WsArtifactFlinkJarService';
import {WsProjectService} from '@/services/project/WsProjectService';
import {WsArtifact, WsArtifactFlinkJar, WsProject} from '@/services/project/typings';

const DataDevelopFlinkJarHistoryWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [project, setProject] = useState<WsProject>({});
  const flinkArtifact = urlParams.state as WsArtifact;

  useEffect(() => {
    WsProjectService.selectOne(flinkArtifact.projectId as number).then((d) => {
      setProject(d);
    });
  }, []);

  const tableColumns: ProColumns<WsArtifactFlinkJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.fileName'}),
      dataIndex: 'fileName',
      hideInSearch: true,
      width: 240,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      dataIndex: 'flinkVersion',
      hideInSearch: true,
      width: 120,
      render: (text, record, index) => {
        return record.flinkVersion?.label;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.entryClass'}),
      dataIndex: 'entryClass',
      hideInSearch: true,
      width: 240,
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.jarParams'}),
      dataIndex: 'jarParams',
      hideInSearch: true,
      width: 240,
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
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined/>}
                  onClick={() => {
                    WsArtifactFlinkJarService.download(record);
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
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
                        WsArtifactFlinkJarService.deleteOne(record).then((d) => {
                          if (d.success) {
                            message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
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
      <div style={{backgroundColor: 'white'}}>
        <PageHeader
          title={intl.formatMessage({id: 'pages.project.artifact.jar'})}
          onBack={() => {
            history.back();
          }}
        >
          <Descriptions size="small" column={3} style={{marginLeft: 48}}>
            <Descriptions.Item label={intl.formatMessage({id: 'pages.project'})}>
              {project.projectCode}
            </Descriptions.Item>
            <Descriptions.Item label={intl.formatMessage({id: 'pages.project.artifact.name'})}>
              {flinkArtifact.name}
            </Descriptions.Item>
            <Descriptions.Item label={intl.formatMessage({id: 'app.common.data.remark'})}>
              {flinkArtifact.remark}
            </Descriptions.Item>
          </Descriptions>
        </PageHeader>
      </div>
      <ProTable<WsArtifactFlinkJar>
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        scroll={{x: 1200, y: 480}}
        search={false}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return WsArtifactFlinkJarService.listByArtifact({...params, artifactId: flinkArtifact.id});
        }}
        pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
    </>
  );
};

export default DataDevelopFlinkJarHistoryWeb;
