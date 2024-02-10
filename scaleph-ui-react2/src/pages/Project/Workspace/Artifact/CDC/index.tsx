import React, {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, FolderOpenOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {history, useAccess, useIntl} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {DictDataService} from '@/services/admin/dictData.service';
import {WsFlinkArtifactCDC} from '@/services/project/typings';
import {WsFlinkCDCService} from "@/services/project/WsFlinkCDCService";
import FlinkArtifactCDCForm from "@/pages/Project/Workspace/Artifact/CDC/FlinkArtifactCDCForm";

const FlinkArtifactCDCWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [flinkArtifactCDCFormData, setFlinkArtifactCDCFormData] = useState<{
    visible: boolean;
    data: WsFlinkArtifactCDC
  }>({
    visible: false,
    data: {},
  });
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const tableColumns: ProColumns<WsFlinkArtifactCDC>[] = [
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
      title: intl.formatMessage({id: 'pages.project.artifact.cdc.flinkCDCVersion'}),
      dataIndex: 'flinkCDCVersion',
      width: 120,
      render: (dom, record) => {
        return record.flinkCDCVersion?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkCDCVersion)
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 150,
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
                    setFlinkArtifactCDCFormData({visiable: true, data: record});
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
                    history.push('/workspace/artifact/cdc/dag', record);
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
                        WsFlinkCDCService.deleteOne(record).then((d) => {
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
      <ProTable<WsFlinkArtifactCDC>
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
          WsFlinkCDCService.list({...params, projectId: projectId})
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkArtifactCDCFormData({visiable: true, data: {}});
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
      {flinkArtifactCDCFormData.visiable && (
        <FlinkArtifactCDCForm
          data={flinkArtifactCDCFormData.data}
          visible={flinkArtifactCDCFormData.visiable}
          onCancel={() => {
            setFlinkArtifactCDCFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkArtifactCDCFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
        />
      )}
    </div>
  );
};

export default FlinkArtifactCDCWeb;
