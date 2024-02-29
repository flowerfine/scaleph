import {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, EditOutlined, FolderOpenOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {history, useAccess, useIntl} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {WsArtifactFlinkSql} from '@/services/project/typings';
import DataDevelopFlinkSQLForm from './DataDevelopFlinkSQLForm';
import {DictDataService} from "@/services/admin/dictData.service";
import {WsArtifactFlinkSqlService} from "@/services/project/WsArtifactFlinkSqlService";

const DataDevelopFlinkSQLWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const [artifactFlinkSqlFormData, setArtifactFlinkSqlFormData] = useState<{
    visiable: boolean;
    data: WsArtifactFlinkSql;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<WsArtifactFlinkSql>[] = [
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
                    setArtifactFlinkSqlFormData({visiable: true, data: record});
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
                    history.push('/workspace/data-develop/flink/sql/editor', record);
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
                        WsArtifactFlinkSqlService.deleteArtifact(record.artifact?.id).then((d) => {
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
      <ProTable<WsArtifactFlinkSql>
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
          WsArtifactFlinkSqlService.list({...params, projectId: projectId + ''})
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setArtifactFlinkSqlFormData({visiable: true, data: {}});
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
      {artifactFlinkSqlFormData.visiable && (
        <DataDevelopFlinkSQLForm
          data={artifactFlinkSqlFormData.data}
          visible={artifactFlinkSqlFormData.visiable}
          onCancel={() => {
            setArtifactFlinkSqlFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setArtifactFlinkSqlFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
        />
      )}
    </PageContainer>
  );
};
export default DataDevelopFlinkSQLWeb;
