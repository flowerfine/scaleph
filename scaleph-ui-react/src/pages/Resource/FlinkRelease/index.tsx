import {useAccess, useIntl} from '@umijs/max';
import {useRef, useState} from 'react';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {DeleteOutlined, DownloadOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {DICT_TYPE} from '@/constants/dictType';
import {PRIVILEGE_CODE} from '@/constants/privilegeCode';
import {DictDataService} from '@/services/admin/dictData.service';
import {FlinkReleaseService} from '@/services/resource/flinkRelease.service';
import {FlinkRelease} from '@/services/resource/typings';
import FlinkReleaseForm from './components/FlinkReleaseForm';

const FlinkReleaseResource: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkRelease[]>([]);
  const [flinkReleaseFormData, setFlinkReleaseData] = useState<{
    visiable: boolean;
    data: FlinkRelease;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<FlinkRelease>[] = [
    {
      title: intl.formatMessage({id: 'pages.resource.flinkRelease.version'}),
      dataIndex: 'version',
      render: (text, record, index) => {
        return record.version?.label;
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.resource.fileName'}),
      dataIndex: 'fileName',
      width: 280,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.path'}),
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      hideInSearch: true,
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
                  icon={<DownloadOutlined></DownloadOutlined>}
                  onClick={() => FlinkReleaseService.download(record)}
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
                        FlinkReleaseService.deleteOne(record).then((d) => {
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
    <PageContainer title={false}>
      <ProTable<FlinkRelease>
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
          return FlinkReleaseService.list(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setFlinkReleaseData({visiable: true, data: {}})}
              >
                {intl.formatMessage({id: 'app.common.operate.upload.label'})}
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
                      FlinkReleaseService.deleteBatch(selectedRows).then((d) => {
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
      {flinkReleaseFormData.visiable && (
        <FlinkReleaseForm
          visible={flinkReleaseFormData.visiable}
          onCancel={() => {
            setFlinkReleaseData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setFlinkReleaseData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={flinkReleaseFormData.data}
        />
      )}
    </PageContainer>
  );
};

export default FlinkReleaseResource;
