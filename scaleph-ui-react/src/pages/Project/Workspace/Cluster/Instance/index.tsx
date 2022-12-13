import { Dict } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkCLusterInstanceService } from '@/services/project/flinkClusterInstance.service';
import { WsFlinkClusterInstance } from '@/services/project/typings';
import { CloseOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Select, Space, Tooltip } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import SessionClusterForm from './components/SessionClusterForm';

const ClusterInstanceWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  const formRef = useRef<ProFormInstance>();
  const [clusterStatusList, setClusterStatusList] = useState<Dict[]>([]);
  const [sessionClusterFormData, setSessionClusterData] = useState<{
    visiable: boolean;
  }>({ visiable: false });

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.flinkClusterStatus).then((d) => {
      setClusterStatusList(d);
    });
  }, []);

  const tableColumns: ProColumns<WsFlinkClusterInstance>[] = [
    {
      title: intl.formatMessage({ id: 'pages.project.cluster.instance.flinkClusterConfigId' }),
      dataIndex: 'flinkClusterConfigId',
      width: 120,
      hideInTable: true,
      hideInSearch: true,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.cluster.instance.name' }),
      dataIndex: 'name',
      width: 180,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.cluster.instance.status' }),
      dataIndex: 'status',
      width: 120,
      render: (text, record, index) => {
        return record.status?.label;
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
            {clusterStatusList.map((item) => {
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
      title: intl.formatMessage({ id: 'pages.project.cluster.instance.clusterId' }),
      dataIndex: 'clusterId',
      width: 280,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.cluster.instance.webInterfaceUrl' }),
      dataIndex: 'webInterfaceUrl',
      width: 240,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.remark' }),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.project.updateTime' }),
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
            {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.close.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<CloseOutlined />}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({ id: 'app.common.operate.close.confirm.title' }),
                      content: intl.formatMessage({
                        id: 'app.common.operate.close.confirm.content',
                      }),
                      okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                      okButtonProps: { danger: true },
                      cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                      onOk() {
                        FlinkCLusterInstanceService.shutdown(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.close.success' }),
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
      <ProTable<WsFlinkClusterInstance>
        headerTitle={intl.formatMessage({ id: 'pages.project.cluster.instance' })}
        search={{
          labelWidth: 'auto',
          span: { xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4 },
        }}
        rowKey="id"
        scroll={{ x: 1200, y: 480 }}
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return FlinkCLusterInstanceService.list({ ...params, projectId: projectId + '' });
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => {
                  setSessionClusterData({ visiable: true });
                }}
              >
                {intl.formatMessage({ id: 'app.common.operate.new.label' })}
              </Button>
            ),
          ],
        }}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      ></ProTable>
      {sessionClusterFormData.visiable && (
        <SessionClusterForm
          visible={sessionClusterFormData.visiable}
          onCancel={() => {
            setSessionClusterData({ visiable: false });
          }}
          onVisibleChange={(visiable) => {
            setSessionClusterData({ visiable: visiable });
            actionRef.current?.reload();
          }}
          data
        />
      )}
    </div>
  );
};

export default ClusterInstanceWeb;
