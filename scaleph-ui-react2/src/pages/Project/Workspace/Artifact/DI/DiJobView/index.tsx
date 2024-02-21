import React, {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, EditOutlined, NodeIndexOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {history, useAccess, useIntl} from '@umijs/max';
import {WORKSPACE_CONF} from '@/constants/constant';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {DictDataService} from '@/services/admin/dictData.service';
import {WsArtifactSeaTunnel} from '@/services/project/typings';
import ArtifactSeaTunnelForm from './ArtifactSeaTunnelForm';
import {WsArtifactSeaTunnelService} from "@/services/project/WsArtifactSeaTunnelService";

const ArtifactSeaTunnelWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [artifactSeaTunnelFormData, setArtifactSeaTunnelFormData] = useState<{
    visiable: boolean;
    data: WsArtifactSeaTunnel
  }>({
    visiable: false,
    data: {},
  });
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onDag = (record: WsArtifactSeaTunnel) => {
    history.push("/workspace/artifact/seatunnel/dag", record)
  }

  const tableColumns: ProColumns<WsArtifactSeaTunnel>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.artifact.name'}),
      dataIndex: 'name',
      width: 240,
      render: (dom, record) => {
        return record.artifact?.name
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.artifact.seatunnel.seaTunnelEngine'}),
      dataIndex: 'seaTunnelEngine',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.seaTunnelEngine?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelEngineType)
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
      title: intl.formatMessage({id: 'pages.project.artifact.seatunnel.seaTunnelVersion'}),
      dataIndex: 'seaTunnelVersion',
      align: 'center',
      width: 100,
      render: (_, record) => {
        return record.seaTunnelVersion?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelVersion)
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 150,
      render: (dom, record) => {
        return record.artifact?.remark
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
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    setArtifactSeaTunnelFormData({visiable: true, data: record});
                  }}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
              <Tooltip title={intl.formatMessage({id: 'pages.project.di.define'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<NodeIndexOutlined/>}
                  onClick={() => onDag(record)}
                />
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
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
                        WsArtifactSeaTunnelService.deleteArtifact(record.artifact?.id).then((d) => {
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
    <div>
      <ProTable<WsArtifactSeaTunnel>
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
          WsArtifactSeaTunnelService.list({...params, projectId: projectId})
        }
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setArtifactSeaTunnelFormData({visiable: true, data: {}});
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
      {artifactSeaTunnelFormData.visiable && (
        <ArtifactSeaTunnelForm
          visible={artifactSeaTunnelFormData.visiable}
          data={artifactSeaTunnelFormData.data}
          onCancel={() => {
            setArtifactSeaTunnelFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visible) => {
            setArtifactSeaTunnelFormData({visiable: visible, data: {}});
          }}
          onOK={(data) => {
            if (data?.id) {
              onDag(data)
            }
          }}
        />
      )}
    </div>
  );
};

export default ArtifactSeaTunnelWeb;
