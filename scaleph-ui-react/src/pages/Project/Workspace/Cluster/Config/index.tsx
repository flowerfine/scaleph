import {DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF} from '@/constant';
import {DictDataService} from '@/services/admin/dictData.service';
import {FlinkClusterConfigService} from '@/services/project/flinkClusterConfig.service';
import {FlinkCLusterInstanceService} from '@/services/project/flinkClusterInstance.service';
import {WsFlinkClusterConfig, WsFlinkClusterInstanceParam} from '@/services/project/typings';
import {DeleteOutlined, DeploymentUnitOutlined, EditOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Popconfirm, Space, Tooltip} from 'antd';
import {useRef} from 'react';
import {history, useAccess, useIntl} from 'umi';

const FlinkClusterConfigWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
  // const [selectedRows, setSelectedRows] = useState<FlinkClusterConfig[]>([]);

  const tableColumns: ProColumns<WsFlinkClusterConfig>[] = [
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.name'}),
      dataIndex: 'name',
      width: 200,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.flinkVersion'}),
      dataIndex: 'flinkVersion',
      width: 120,
      render: (text, record, index) => {
        return record.flinkVersion?.label;
      },
      request: () => DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
    },
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.deployMode'}),
      dataIndex: 'deployMode',
      width: 120,
      render: (text, record, index) => {
        return record.deployMode?.label;
      },
      request: () => DictDataService.listDictDataByType2(DICT_TYPE.flinkDeploymentMode)
    },
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.resourceProvider'}),
      dataIndex: 'resourceProvider',
      width: 120,
      render: (text, record, index) => {
        return record.resourceProvider?.label;
      },
      request: () => DictDataService.listDictDataByType2(DICT_TYPE.flinkResourceProvider)
    },
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.flinkRelease'}),
      dataIndex: 'flinkRelease',
      width: 240,
      hideInSearch: true,
      render: (text, record, index) => {
        return record.flinkRelease?.fileName;
      },
    },
    {
      title: intl.formatMessage({id: 'page.project.cluster.config.clusterCredential'}),
      dataIndex: 'clusterCredential',
      width: 120,
      hideInSearch: true,
      render: (text, record, index) => {
        return record.clusterCredential?.name;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.remark'}),
      dataIndex: 'remark',
      width: 240,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.project.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.project.updateTime'}),
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
              <Popconfirm
                disabled={record.deployMode?.value != '2'}
                title={intl.formatMessage({id: 'pages.project.cluster.instance.create.confirm'})}
                onConfirm={() => {
                  let clusterParams: WsFlinkClusterInstanceParam = {
                    flinkClusterConfigId: record.id,
                    projectId: record.projectId,
                  };
                  FlinkCLusterInstanceService.newSession(clusterParams).then((response) => {
                    if (response.success) {
                      message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                      history.push('/workspace/cluster/instance');
                    }
                  });
                }}
              >
                <Tooltip
                  title={intl.formatMessage({id: 'pages.project.cluster.instance.create'})}
                >
                  <Button
                    shape="default"
                    type="link"
                    disabled={record.deployMode?.value != '2'}
                    icon={<DeploymentUnitOutlined/>}
                  ></Button>
                </Tooltip>
              </Popconfirm>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => {
                    history.push('/workspace/cluster/config/options', record);
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
                        FlinkClusterConfigService.deleteOne(record).then((d) => {
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
    <ProTable<WsFlinkClusterConfig>
      headerTitle={intl.formatMessage({id: 'page.project.cluster.config'})}
      search={{
        labelWidth: 'auto',
        span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
      }}
      rowKey="id"
      scroll={{x: 1200, y: 480}}
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        return FlinkClusterConfigService.list({...params, projectId: projectId + ''});
      }}
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                history.push('/workspace/cluster/config/options', {});
              }}
            >
              {intl.formatMessage({id: 'app.common.operate.new.label'})}
            </Button>
          ),
          // access.canAccess(PRIVILEGE_CODE.datadevProjectDelete) && (
          //   <Button
          //     key="del"
          //     type="default"
          //     disabled={selectedRows.length < 1}
          //     onClick={() => {
          //       Modal.confirm({
          //         title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
          //         content: intl.formatMessage({
          //           id: 'app.common.operate.delete.confirm.content',
          //         }),
          //         okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
          //         okButtonProps: { danger: true },
          //         cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
          //         onOk() {
          //           FlinkClusterConfigService.deleteBatch(selectedRows).then((d) => {
          //             if (d.success) {
          //               message.success(
          //                 intl.formatMessage({ id: 'app.common.operate.delete.success' }),
          //               );
          //               actionRef.current?.reload();
          //             }
          //           });
          //         },
          //       });
          //     }}
          //   >
          //     {intl.formatMessage({ id: 'app.common.operate.delete.label' })}
          //   </Button>
          // ),
        ],
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      // rowSelection={{
      //   fixed: true,
      //   onChange(selectedRowKeys, selectedRows, info) {
      //     setSelectedRows(selectedRows);
      //   },
      // }}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
};

export default FlinkClusterConfigWeb;
