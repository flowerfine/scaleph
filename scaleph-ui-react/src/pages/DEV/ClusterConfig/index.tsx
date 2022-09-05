import {PRIVILEGE_CODE} from '@/constant';
import {deleteProjectBatch, deleteProjectRow,} from '@/services/project/project.service';
import {DeleteOutlined, EditOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {useAccess, useIntl} from 'umi';
import FlinkClusterConfigForm from './components/FlinkClusterConfigForm';
import {FlinkClusterConfig} from "@/services/dev/typings";
import {list} from "@/services/dev/flinkClusterConfig.service";

const FlinkClusterConfigWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkClusterConfig[]>([]);
  const [flinkClusterConfigFormData, setFlinkClusterConfigFormData] = useState<{
    visiable: boolean;
    data: FlinkClusterConfig;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<FlinkClusterConfig>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'}),
      dataIndex: 'flinkVersion',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'}),
      dataIndex: 'deployMode',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'}),
      dataIndex: 'resourceProvider',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'}),
      dataIndex: 'flinkRelease',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'}),
      dataIndex: 'clusterCredential',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.configOptions'}),
      dataIndex: 'configOptions',
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
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    setFlinkClusterConfigFormData({visiable: true, data: record});
                  }}
                ></Button>
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
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        deleteProjectRow(record).then((d) => {
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
      <ProTable<FlinkClusterConfig>
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
            access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setFlinkClusterConfigFormData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevProjectDelete) && (
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
                      deleteProjectBatch(selectedRows).then((d) => {
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
      {flinkClusterConfigFormData.visiable && (
        <FlinkClusterConfigForm
          visible={flinkClusterConfigFormData.visiable}
          onCancel={() => {
            setFlinkClusterConfigFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkClusterConfigFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={flinkClusterConfigFormData.data}
        />
      )}
    </div>
  );
};

export default FlinkClusterConfigWeb;
