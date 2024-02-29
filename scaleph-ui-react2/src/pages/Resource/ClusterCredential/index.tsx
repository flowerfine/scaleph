import {useAccess, useIntl} from 'umi';
import {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, DownloadOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {ClusterCredentialService} from '@/services/resource/clusterCredential.service';
import {ClusterCredential} from '@/services/resource/typings';
import ClusterCredentialForm from './components/ClusterCredentialForm';

const ClusterCredentialResource: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<ClusterCredential[]>([]);
  const [clusterCredentialFormData, setClusterCredentialData] = useState<{
    visiable: boolean;
    data: ClusterCredential;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<ClusterCredential>[] = [
    {
      title: intl.formatMessage({id: 'pages.resource.clusterCredential.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.resource.clusterCredential.context'}),
      dataIndex: 'context',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.resource.clusterCredential.fileName'}),
      dataIndex: 'fileName',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.resource.clusterCredential.path'}),
      dataIndex: 'path',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true
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
            {access.canAccess(PRIVILEGE_CODE.workspaceJobShow) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined/>}
                  onClick={() => {
                    ClusterCredentialService.download(record);
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
                        ClusterCredentialService.deleteOne(record).then((d) => {
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
      <ProTable<ClusterCredential>
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
          return ClusterCredentialService.list(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setClusterCredentialData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
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
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      ClusterCredentialService.deleteBatch(selectedRows).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
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
      />
      {clusterCredentialFormData.visiable && (
        <ClusterCredentialForm
          visible={clusterCredentialFormData.visiable}
          onCancel={() => {
            setClusterCredentialData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setClusterCredentialData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={clusterCredentialFormData.data}
        />
      )}
    </div>
  );
};

export default ClusterCredentialResource;
