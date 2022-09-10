import {DICT_TYPE, PRIVILEGE_CODE} from '@/constant';
import {DeleteOutlined, EditOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Select, Space, Tooltip} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {history, useAccess, useIntl} from 'umi';
import {FlinkClusterConfig} from "@/services/dev/typings";
import {deleteBatch, deleteOne, list} from "@/services/dev/flinkClusterConfig.service";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {Dict} from '@/app.d';

const FlinkClusterConfigWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkClusterConfig[]>([]);
  const [flinkVersionList, setFlinkVersionList] = useState<Dict[]>([]);
  const [resourceProviderList, setResourceProviderList] = useState<Dict[]>([]);
  const [deployModeList, setDeployModeList] = useState<Dict[]>([]);
  
  useEffect(() => {
    listDictDataByType(DICT_TYPE.flinkVersion).then((d) => {
      setFlinkVersionList(d);
    });
    listDictDataByType(DICT_TYPE.flinkResourceProvider).then((d) => {
      setResourceProviderList(d);
    });
    listDictDataByType(DICT_TYPE.flinkDeploymentMode).then((d) => {
      setDeployModeList(d);
    });
  }, []);

  const tableColumns: ProColumns<FlinkClusterConfig>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'}),
      dataIndex: 'flinkVersion',
      render: (text, record, index) => {
        return record.flinkVersion?.label;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {flinkVersionList.map((item) => {
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
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'}),
      dataIndex: 'deployMode',
      render: (text, record, index) => {
        return record.deployMode?.label;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {deployModeList.map((item) => {
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
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'}),
      dataIndex: 'resourceProvider',
      render: (text, record, index) => {
        return record.resourceProvider?.label;
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {resourceProviderList.map((item) => {
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
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'}),
      dataIndex: 'flinkRelease',
      hideInSearch: true,
      render: (text, record, index) => {
        return record.flinkRelease?.fileName;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'}),
      dataIndex: 'clusterCredential',
      hideInSearch: true,
      render: (text, record, index) => {
        return record.clusterCredential?.name;
      },
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
                    history.push("/workspace/dev/clusterConfigOptions", record);
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
                        deleteOne(record).then((d) => {
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
                history.push("/workspace/dev/clusterConfigOptions", {});
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
                    deleteBatch(selectedRows).then((d) => {
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
    />
  );
};

export default FlinkClusterConfigWeb;
