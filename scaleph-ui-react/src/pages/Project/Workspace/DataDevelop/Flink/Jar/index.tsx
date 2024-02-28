import {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, DownloadOutlined, EditOutlined, FolderOpenOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {history, useAccess, useIntl} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {WsArtifactFlinkJar} from '@/services/project/typings';
import {WsArtifactFlinkJarService} from "@/services/project/WsArtifactFlinkJarService";
import {DictDataService} from "@/services/admin/dictData.service";
import DataDevelopFlinkJarAddForm from './DataDevelopFlinkJarAddForm';
import DataDevelopFlinkJarUpdateForm from "./DataDevelopFlinkJarUpdateForm";

const DataDevelopFlinkJarWeb: React.FC = () => {
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
    data: WsArtifactFlinkJar;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<WsArtifactFlinkJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      dataIndex: 'name',
      width: 240,
      render: (dom, entity, index, action, schema) => {
        return entity.artifact?.name
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
        return entity.artifact?.remark
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
                    WsArtifactFlinkJarService.download(record);
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
                    history.push('/workspace/data-develop/flink/jar/history', record.artifact);
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
                        WsArtifactFlinkJarService.deleteArtifact(record.artifact?.id).then((d) => {
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
      <ProTable<WsArtifactFlinkJar>
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
          WsArtifactFlinkJarService.list({...params, projectId: projectId + ''})
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
        <DataDevelopFlinkJarAddForm
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
        <DataDevelopFlinkJarUpdateForm
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
    </PageContainer>
  );
};
export default DataDevelopFlinkJarWeb;
