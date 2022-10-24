import {PRIVILEGE_CODE} from '@/constant';
import {FLinkJobInstanceJarService} from '@/services/dev/flinkJobInstanceJar.service';
import {EditOutlined, PlaySquareOutlined, ProfileOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Modal, Space, Tooltip} from 'antd';
import {useRef, useState} from 'react';
import {history, useAccess, useIntl} from 'umi';
import {FlinkJobForJar, FlinkJobListByTypeParam} from "@/pages/DEV/Job/typings";
import {FlinkJobService} from "@/pages/DEV/Job/FlinkJobService";

const JobForJarWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<FlinkJobForJar[]>([]);

  const tableColumns: ProColumns<FlinkJobForJar>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.job.name'}),
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.artifact'}),
      dataIndex: 'flinkArtifactJar',
      render: (text, record, index) => {
        return `${record.flinkArtifactJar?.flinkArtifact?.name}/${record.flinkArtifactJar?.version}/${record.flinkArtifactJar?.fileName}`;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterConfig'}),
      dataIndex: 'flinkClusterConfig',
      render: (text, record, index) => {
        return record.flinkClusterConfig?.name;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.clusterInstance'}),
      dataIndex: 'flinkClusterInstance',
      render: (text, record, index) => {
        return record.flinkClusterInstance?.name;
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
                    history.push('/workspace/dev/job/jar/options', record);
                  }}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.submit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<PlaySquareOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.submit.confirm.title'}),
                      content: intl.formatMessage({id: 'app.common.operate.submit.confirm.content'}),
                      okText: intl.formatMessage({id: 'app.common.operate.submit.label'}),
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        FLinkJobInstanceJarService.submit(record).then((d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({id: 'app.common.operate.submit.success'}),
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
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.detail.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<ProfileOutlined />}
                  onClick={() => {
                    history.push("/workspace/dev/job/jar/detail", record)
                  }}/>
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];
  return (
    <ProTable<FlinkJobForJar>
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
        const param: FlinkJobListByTypeParam = {
          ...params,
          type: '0'
        }
        return FlinkJobService.listJobsForJar(param);
      }}
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevProjectAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => {
                history.push('/workspace/dev/job/jar/options');
              }}
            >
              {intl.formatMessage({id: 'app.common.operate.new.label'})}
            </Button>
          )
        ]
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

export default JobForJarWeb;
