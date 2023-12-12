import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {WsFlinkArtifactSql} from '@/services/project/typings';
import {DeleteOutlined, EditOutlined, FolderOpenOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {useRef, useState} from 'react';
import {history, useAccess, useIntl} from 'umi';
import FlinkArtifactSqlForm from '@/pages/Project/Workspace/Artifact/Sql/FlinkArtifactSqlForm';
import {DictDataService} from "@/services/admin/dictData.service";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";

const JobArtifactSqlView: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [flinkArtifactSqlFormData, setFlinkArtifactSqlFormData] = useState<{
    visiable: boolean;
    data: WsFlinkArtifactSql;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<WsFlinkArtifactSql>[] = [
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
      render: (dom, record) => {
        return record.flinkVersion?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
      }
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
                    setFlinkArtifactSqlFormData({visiable: true, data: record});
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
                    history.push('/workspace/artifact/editor', record);
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
                        FlinkArtifactSqlService.deleteAll(record.wsFlinkArtifact.id).then((d) => {
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
      <ProTable<WsFlinkArtifactSql>
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
          FlinkArtifactSqlService.list({...params, projectId: projectId + ''})
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactSqlFormData({visiable: true, data: {}});
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
      {flinkArtifactSqlFormData.visiable && (
        <FlinkArtifactSqlForm
          data={flinkArtifactSqlFormData.data}
          visible={flinkArtifactSqlFormData.visiable}
          onCancel={() => {
            setFlinkArtifactSqlFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactSqlFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
        />
      )}
    </div>
  );
};
export default JobArtifactSqlView;
