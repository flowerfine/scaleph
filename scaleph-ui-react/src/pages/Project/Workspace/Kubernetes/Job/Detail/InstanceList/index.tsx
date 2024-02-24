import React, {useRef} from "react";
import {Tooltip} from "antd";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {connect, useAccess, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstance} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobDetailInstanceListWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const tableColumns: ProColumns<WsFlinkKubernetesJobInstance>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList.startTime'}),
      dataIndex: 'startTime'
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList.endTime'}),
      dataIndex: 'endTime'
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList.duration'}),
      dataIndex: 'duration'
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList.upgradeMode'}),
      dataIndex: 'upgradeMode',
      render: (dom, record) => {
        return <Tooltip title={record.upgradeMode?.remark}>{record.upgradeMode?.label}</Tooltip>
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.state'}),
      dataIndex: 'state',
      render: (dom, record) => {
        return <Tooltip title={record.state?.remark}>{record.state?.label}</Tooltip>
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.jobState'}),
      dataIndex: 'jobState',
      render: (dom, record) => {
        return <Tooltip title={record.jobState?.value}>{record.jobState?.label}</Tooltip>
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.error'}),
      dataIndex: 'error'
    },
  ];

  return (<div>
    <ProTable<WsFlinkKubernetesJobInstance>
      search={false}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) =>
        WsFlinkKubernetesJobService.listInstances({...params, wsFlinkKubernetesJobId: props.flinkKubernetesJobDetail.job?.id})
      }
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      rowSelection={false}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  </div>);
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailInstanceListWeb);
