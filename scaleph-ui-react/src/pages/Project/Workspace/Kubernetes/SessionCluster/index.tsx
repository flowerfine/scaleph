import {history, useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, EyeOutlined} from "@ant-design/icons";
import {
  ActionType,
  ProColumns,
  ProFormInstance,
  ProFormSelect,
  ProFormSwitch,
  ProTable
} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {DictDataService} from "@/services/admin/dictData.service";
import {YesOrNo} from "@/constants/enum";

const FlinkKubernetesSessionClusterWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<WsFlinkKubernetesSessionCluster[]>([]);
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const supportSqlGateway = (record: WsFlinkKubernetesSessionCluster, checked: boolean) => {
    if (checked) {
      WsFlinkKubernetesSessionClusterService.enableSqlGateway(record).then(response => {
        actionRef.current?.reload()
      })
    } else {
      WsFlinkKubernetesSessionClusterService.disableSqlGateway(record).then(response => {
        actionRef.current?.reload()
      })
    }
  }

  const tableColumns: ProColumns<WsFlinkKubernetesSessionCluster>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.name'}),
      dataIndex: 'name',
      width: '10%',
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.flinkVersion'}),
      dataIndex: 'flinkVersion',
      hideInSearch: true,
      render: (dom, entity) => {
        return (<Tag>{entity.kubernetesOptions?.flinkVersion}</Tag>)
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)}
          />
        );
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.image'}),
      dataIndex: 'image',
      hideInSearch: true,
      render: (dom, entity) => {
        return entity.kubernetesOptions?.image
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.namespace'}),
      dataIndex: 'namespace',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.sql-gateway'}),
      dataIndex: 'supportSqlGateway',
      width: 100,
      hideInSearch: true,
      render: (dom, record) => {
        return <ProFormSwitch
          fieldProps={{
            checked: record.supportSqlGateway?.value == '1' ? true : false,
            onChange: (checked) => supportSqlGateway(record, checked)
          }}
        />
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.yesNo)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.state'}),
      dataIndex: 'state',
      width: 100,
      render: (dom, record) => {
        return <Tooltip title={record.state?.remark}>{record.state?.label}</Tooltip>
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.resourceLifecycleState)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.error'}),
      dataIndex: 'error',
      hideInSearch: true,
      ellipsis: true
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
      width: '8%',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: '8%',
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <Space>
          {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
              <Button
                shape="default"
                type="link"
                icon={<EditOutlined/>}
                disabled={record.deployed?.value == YesOrNo.YES}
                onClick={() => {
                  history.push("/workspace/flink/kubernetes/session-cluster/steps/update", record)
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.detail.label'})}>
              <Button
                shape="default"
                type="link"
                icon={<EyeOutlined/>}
                onClick={() => history.push("/workspace/flink/kubernetes/session-cluster/detail", record)}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevDatasourceDelete) && (
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
              <Button
                shape="default"
                type="link"
                danger
                icon={<DeleteOutlined/>}
                disabled={record.deployed?.value == YesOrNo.YES}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      WsFlinkKubernetesSessionClusterService.delete(record).then((d) => {
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
      ),
    },
  ];

  return (<div>
    <ProTable<WsFlinkKubernetesSessionCluster>
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
        WsFlinkKubernetesSessionClusterService.list({...params, projectId: projectId})
      }
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                history.push("/workspace/flink/kubernetes/session-cluster/steps/new")
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
                    WsFlinkKubernetesSessionClusterService.deleteBatch(selectedRows).then((d) => {
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
          )
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
  </div>);
}

export default FlinkKubernetesSessionClusterWeb;

