import React, {useRef} from "react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstance} from "@/services/project/typings";
import {connect, useAccess, useIntl} from "umi";
import {ActionType, ProColumns, ProFormInstance, ProFormSelect, ProTable} from "@ant-design/pro-components";
import {Tag, Tooltip} from "antd";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
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
      render: (dom, entity) => {
        return (<Tag>{entity.upgradeMode?.label}</Tag>)
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <ProFormSelect
            showSearch={false}
            allowClear={true}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.upgradeMode)}
          />
        );
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.state'}),
      dataIndex: 'state',
      render: (dom, record) => {
        return <Tooltip title={record.state?.remark}>{record.state?.label}</Tooltip>
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.resourceLifecycleState)
      },
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.jobState'}),
      dataIndex: 'jobState',
      render: (dom, record) => {
        return <Tooltip title={record.jobState?.value}>{record.jobState?.label}</Tooltip>
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.flinkJobStatus)
      },
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
        WsFlinkKubernetesJobService.listInstances({...params, wsFlinkKubernetesJobId: props.jobDetail.job?.id})
      }
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      rowSelection={false}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  </div>);
}


const mapModelToProps = ({jobDetail}: any) => ({jobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailInstanceListWeb);
