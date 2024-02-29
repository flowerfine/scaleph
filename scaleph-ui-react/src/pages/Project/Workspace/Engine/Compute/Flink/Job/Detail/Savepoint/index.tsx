import React, {useRef} from "react";
import {Tag, Tooltip} from "antd";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {connect, useAccess, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstanceSavepoint} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import moment from "moment";

const FlinkKubernetesJobDetailSavepointWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const tableColumns: ProColumns<WsFlinkKubernetesJobInstanceSavepoint>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint.timeStamp'}),
      dataIndex: 'timeStamp',
      render: (dom, record) => {
        return moment(record.timeStamp).format("YYYY-MM-DD HH:mm:ss")
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint.formatType'}),
      dataIndex: 'formatType',
      render: (dom, record) => {
        return (<Tag>{record.formatType?.label}</Tag>)
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint.triggerType'}),
      dataIndex: 'triggerType',
      render: (dom, record) => {
        return <Tooltip title={record.triggerType?.remark}>{record.triggerType?.label}</Tooltip>
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint.location'}),
      dataIndex: 'location'
    },
  ];

  return (<div>
    <ProTable<WsFlinkKubernetesJobInstanceSavepoint>
      search={false}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        if (props.flinkKubernetesJobDetail.job?.jobInstance?.id) {
          return WsFlinkKubernetesJobService.listSavepoints({
            ...params,
            wsFlinkKubernetesJobInstanceId: props.flinkKubernetesJobDetail.job?.jobInstance?.id
          })
        }
        return Promise.reject()
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      rowSelection={false}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  </div>);
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailSavepointWeb);
