import {history, useAccess, useIntl} from "umi";
import {useRef, useState} from "react";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {FlinkJobConfigJar} from "@/services/dev/typings";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {PRIVILEGE_CODE} from "@/constant";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {deleteBatch, deleteOne, list} from "@/services/dev/flinkJobConfigJar.service";

const JobConfigJarWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkJobConfigJar[]>([]);

  const tableColumns: ProColumns<FlinkJobConfigJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.jobConfigJar.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact'}),
      dataIndex: 'flinkArtifactJar',
      render: (text, record, index) => {
        return `${record.flinkArtifactJar?.flinkArtifact.name}/${record.flinkArtifactJar?.version}/${record.flinkArtifactJar?.fileName}`;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig'}),
      dataIndex: 'flinkClusterConfig',
      render: (text, record, index) => {
        return record.flinkClusterConfig?.name;
      }
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterInstance'}),
      dataIndex: 'flinkClusterInstance',
      render: (text, record, index) => {
        return record.flinkClusterInstance?.name;
      }
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
                    history.push("/workspace/dev/jobConfigJar/options", record);
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
    <ProTable<FlinkJobConfigJar>
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
                history.push("/workspace/dev/jobConfigJar/options");
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
}

export default JobConfigJarWeb;
