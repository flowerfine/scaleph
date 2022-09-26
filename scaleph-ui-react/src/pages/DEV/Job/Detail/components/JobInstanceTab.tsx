import {useAccess, useIntl} from "umi";
import {useRef} from "react";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {FlinkJobInstance, FlinkJobInstanceListParam} from "@/pages/DEV/Job/typings";
import {Button, Space, Tooltip} from "antd";
import {PRIVILEGE_CODE} from "@/constant";
import {EditOutlined} from "@ant-design/icons";
import {history} from "@@/core/history";
import {FlinkJobInstanceService} from "@/pages/DEV/Job/FlinkJobInstanceService";

const JobInstanceWeb: React.FC<{flinkJobCode: number}> = ({flinkJobCode}) => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const tableColumns: ProColumns<FlinkJobInstance>[] = [
    {
      title: intl.formatMessage({id: 'pages.dev.job.version'}),
      dataIndex: 'flinkJobVersion',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.job.jobId'}),
      dataIndex: 'jobId',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.job.jobName'}),
      dataIndex: 'jobName',
    },
    {
      title: intl.formatMessage({id: 'pages.dev.job.jobState'}),
      dataIndex: 'jobState',
      render: (text, record, index) => {
        return record.jobState?.label;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.job.clusterStatus'}),
      dataIndex: 'clusterStatus',
      render: (text, record, index) => {
        return record.clusterStatus?.label;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dev.createTime'}),
      dataIndex: 'createTime',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dev.updateTime'}),
      dataIndex: 'updateTime',
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
          </Space>
        </>
      ),
    },
  ];

  return (
    <ProTable<FlinkJobInstance>
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      search={false}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        const param: FlinkJobInstanceListParam = {
          ...params,
          flinkJobCode: flinkJobCode
        }
        return FlinkJobInstanceService.list(param);
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
}

export default JobInstanceWeb;
