import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {WsFlinkArtifactJar} from '@/services/project/typings';
import {DeleteOutlined, DownloadOutlined, EditOutlined, FolderOpenOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {useRef, useState} from 'react';
import {history, useAccess, useIntl} from 'umi';
import FlinkArtifactJarAddForm from './FlinkArtifactJarAddForm';
import {FlinkArtifactJarService} from "@/services/project/flinkArtifactJar.service";
import {DictDataService} from "@/services/admin/dictData.service";
import FlinkArtifactJarUpdateForm from "@/pages/Project/Workspace/Artifact/Jar/FlinkArtifactJarUpdateForm";

const ArtifactJarView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [flinkArtifactJarAddFormData, setFlinkArtifactJarAddFormData] = useState<{
    visiable: boolean;
  }>({visiable: false});
  const [flinkArtifactJarUpdateFormData, setFlinkArtifactJarUpdateFormData] = useState<{
    visiable: boolean;
    data: WsFlinkArtifactJar;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<WsFlinkArtifactJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      dataIndex: 'name',
      width: 240,
      render: (dom, entity, index, action, schema) => {
        return entity.wsFlinkArtifact?.name
      }
    },
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      dataIndex: 'flinkVersion',
      width: 120,
      render: (dom, record, index) => {
        return record.flinkVersion?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.jar.fileName'}),
      dataIndex: 'fileName',
      hideInSearch: true,
      width: 240
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
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      width: 240,
      hideInSearch: true,
      render: (dom, entity, index, action, schema) => {
        return entity.wsFlinkArtifact?.remark
      }
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
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    setFlinkArtifactJarUpdateFormData({visiable: true, data: record});
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined/>}
                  onClick={() => {
                    FlinkArtifactJarService.download(record);
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.workspaceJobShow) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.more.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FolderOpenOutlined/>}
                  onClick={() => {
                    history.push('/workspace/artifact/history', record.wsFlinkArtifact);
                  }}
                />
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
                        FlinkArtifactJarService.deleteAll(record.wsFlinkArtifact.id).then((d) => {
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
      <ProTable<WsFlinkArtifactJar>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) =>
          FlinkArtifactJarService.list({...params, projectId: projectId + ''})
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactJarAddFormData({visiable: true});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
          ],
        }}
        pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {flinkArtifactJarAddFormData.visiable && (
        <FlinkArtifactJarAddForm
          visible={flinkArtifactJarAddFormData.visiable}
          onCancel={() => {
            setFlinkArtifactJarAddFormData({visiable: false});
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactJarAddFormData({visiable: visiable});
            actionRef.current?.reload();
          }}
        />
      )}
      {flinkArtifactJarUpdateFormData.visiable && (
        <FlinkArtifactJarUpdateForm
          visible={flinkArtifactJarUpdateFormData.visiable}
          onCancel={() => {
            setFlinkArtifactJarUpdateFormData({visiable: false});
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactJarUpdateFormData({visiable: visiable});
            actionRef.current?.reload();
          }}
          data={flinkArtifactJarUpdateFormData.data}
        />
      )}
    </div>
  );
};
export default ArtifactJarView;
