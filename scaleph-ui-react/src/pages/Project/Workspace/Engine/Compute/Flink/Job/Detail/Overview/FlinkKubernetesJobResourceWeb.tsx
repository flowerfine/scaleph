import React from "react";
import {ProDescriptions} from "@ant-design/pro-components";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {connect, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob, WsFlinkKubernetesJobInstance} from "@/services/project/typings";

const FlinkKubernetesJobResourceWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();

  const descriptionColumns: ProDescriptionsItemProps<WsFlinkKubernetesJobInstance>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerCpu'}),
      key: `jobManagerCpu`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.jobManager?.resource?.cpu
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerCpu'}),
      key: `taskManagerCpu`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.taskManager?.resource?.cpu
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerMemory'}),
      key: `jobManagerMemory`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.jobManager?.resource?.memory
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerMemory'}),
      key: `taskManagerMemory`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.taskManager?.resource?.memory
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.jobManagerReplicas'}),
      key: `jobManagerReplicas`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.jobManager?.replicas
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.resource.taskManagerReplicas'}),
      key: `taskManagerReplicas`,
      renderText: (text: any, record: WsFlinkKubernetesJobInstance, index: number, action: ProCoreActionType) => {
        return record.taskManager?.replicas
      }
    },
  ]

  return (
    <ProDescriptions
      column={2}
      bordered={true}
      dataSource={props.flinkKubernetesJobDetail.job?.jobInstance}
      columns={descriptionColumns}
    />
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobResourceWeb);
