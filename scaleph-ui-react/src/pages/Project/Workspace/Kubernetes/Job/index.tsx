import {history, useAccess, useIntl} from "umi";
import React, {useRef, useState} from "react";
import {Button, message, Modal, Space, Tag, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, NodeIndexOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProFormSelect, ProTable} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DICT_TYPE} from "@/constants/dictType";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import FlinkKubernetesJobForm from "@/pages/Project/Workspace/Kubernetes/Job/JobForm";

const FlinkKubernetesJobWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<WsFlinkKubernetesJob[]>([]);
  const [jobFormData, setJobFormData] = useState<{
    visiable: boolean;
    data: WsFlinkKubernetesJob;
  }>({visiable: false, data: {}});
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const tableColumns: ProColumns<WsFlinkKubernetesJob>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.name'}),
      dataIndex: 'name',
      width: '10%'
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.executionMode'}),
      dataIndex: 'executionMode',
      render: (dom, entity) => {
        return (<Tag>{entity.executionMode?.label}</Tag>)
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkRuntimeExecutionMode)}
          />
        );
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.deploymentKind'}),
      dataIndex: 'deploymentKind',
      render: (dom, entity) => {
        return (<Tag>{entity.deploymentKind?.label}</Tag>)
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.deploymentKind)}
          />
        );
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.type'}),
      dataIndex: 'type',
      render: (dom, entity) => {
        return (<Tag>{entity.type?.label}</Tag>)
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkJobType)}
          />
        );
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.artifact'}),
      dataIndex: 'artifact',
      hideInSearch: true,
      render: (dom, entity) => {
        return entity.flinkArtifactJar ? entity.flinkArtifactJar.wsFlinkArtifact?.name : (entity.flinkArtifactSql ? entity.flinkArtifactSql?.wsFlinkArtifact?.name : entity.wsDiJob?.wsFlinkArtifact?.name)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.state'}),
      dataIndex: 'state',
      hideInSearch: true,
      render: (dom, record) => {
        return <Tooltip title={record.jobInstance?.state?.remark}>{record.jobInstance?.state?.label}</Tooltip>
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.resourceLifecycleState)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.jobState'}),
      dataIndex: 'jobState',
      hideInSearch: true,
      render: (dom, record) => {
        return <Tooltip title={record.jobInstance?.jobState?.value}>{record.jobInstance?.jobState?.label}</Tooltip>
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkJobStatus)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.error'}),
      dataIndex: 'error',
      hideInSearch: true,
      render: (dom, record) => {
        return record.jobInstance?.error
      },
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
                onClick={() => {
                  setJobFormData({visiable: true, data: record});
                }}
              />
            </Tooltip>
          )}
          {access.canAccess(PRIVILEGE_CODE.datadevJobEdit) && (
            <Tooltip title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail'})}>
              <Button
                shape="default"
                type="link"
                icon={<NodeIndexOutlined/>}
                onClick={() => {
                  history.push("/workspace/flink/kubernetes/job/detail", record)
                }}
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
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      WsFlinkKubernetesJobService.deleteOne(record).then((d) => {
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
    <ProTable<WsFlinkKubernetesJob>
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
        WsFlinkKubernetesJobService.list({...params, projectId: projectId})
      }
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                setJobFormData({visiable: true, data: {}});
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
                    WsFlinkKubernetesJobService.deleteBatch(selectedRows).then((response) => {
                      if (response.success) {
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
    {jobFormData.visiable && (
      <FlinkKubernetesJobForm
        visible={jobFormData.visiable}
        onCancel={() => {
          setJobFormData({visiable: false, data: {}});
        }}
        onVisibleChange={(visiable) => {
          setJobFormData({visiable: visiable, data: {}});
          actionRef.current?.reload();
        }}
        data={jobFormData.data}
      />
    )}
  </div>);
}

export default FlinkKubernetesJobWeb;
