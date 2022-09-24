import { Dict } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { ClusterService } from '@/services/cluster/cluster.service';
import { DiClusterConfig } from '@/services/cluster/typings';
import { DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Select, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import ClusterForm from './components/ClusterForm';

const Cluster: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DiClusterConfig[]>([]);
  const [clusterTypeList, setClusterTypeList] = useState<Dict[]>([]);
  const [clusterFormData, setClusterFormData] = useState<{
    visible: boolean;
    data: DiClusterConfig;
  }>({ visible: false, data: {} });

  const tableColumns: ProColumns<DiClusterConfig>[] = [
    {
      title: intl.formatMessage({ id: 'pages.cluster.clusterName' }),
      dataIndex: 'clusterName',
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.clusterType' }),
      dataIndex: 'clusterType',
      width: 120,
      render: (text, record, index) => {
        return record.clusterType?.label;
      },
      renderFormItem: (item, { defaultRender, ...rest }, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {clusterTypeList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.clusterVersion' }),
      dataIndex: 'clusterVersion',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.clusterHome' }),
      dataIndex: 'clusterHome',
      hideInSearch: true,
      width: 280,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.clusterConf' }),
      dataIndex: 'clusterConf',
      hideInSearch: true,
      hideInTable: true,
      width: 280,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.remark' }),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.cluster.updateTime' }),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevClusterEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => {
                    setClusterFormData({ visible: true, data: record });
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevClusterDelete) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.delete.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined />}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.delete.confirm.content',
                      }),
                      okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                      okButtonProps: { danger: true },
                      cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                      onOk() {
                        ClusterService.deleteClusterRow(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.delete.success' }),
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

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.clusterType).then((d) => {
      setClusterTypeList(d);
    });
  }, []);

  return (
    <div>
      <ProTable<DiClusterConfig>
        headerTitle={intl.formatMessage({ id: 'pages.cluster' })}
        search={{
          labelWidth: 'auto',
          span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return ClusterService.listClusterByPage(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevClusterAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setClusterFormData({ visible: true, data: {} });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevClusterDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                    content: intl.formatMessage({
                      id: 'app.common.operate.delete.confirm.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    okButtonProps: { danger: true },
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      ClusterService.deleteClusterBatch(selectedRows).then((d) => {
                        if (d.success) {
                          message.success(
                            intl.formatMessage({ id: 'app.common.operate.delete.success' }),
                          );
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
              </Button>
            ),
          ],
        }}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        rowSelection={{
          fixed: true,
          onChange(selectedRowKeys, selectedRows, info) {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {clusterFormData.visible && (
        <ClusterForm
          visible={clusterFormData.visible}
          onCancel={() => {
            setClusterFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setClusterFormData({ visible: visible, data: {} });
            actionRef.current?.reload();
          }}
          data={clusterFormData.data}
        />
      )}
    </div>
  );
};

export default Cluster;
