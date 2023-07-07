import {useAccess, useIntl, useLocation} from "umi";
import React from "react";
import {Tabs} from "antd";
import FlinkKubernetesJobDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/YAML";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {PageContainer, ProDescriptions} from "@ant-design/pro-components";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";

const FlinkKubernetesJobDetailWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const urlParams = useLocation()

  const descriptionColumns: ProDescriptionsItemProps<WsFlinkKubernetesJob>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.name'}),
      key: `name`,
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.executionMode'}),
      key: `executionMode`,
      renderText: (text: any, record: Entity, index: number, action: ProCoreActionType) => {
        return record.executionMode?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.deploymentKind'}),
      key: `deploymentKind`,
      renderText: (text: any, record: Entity, index: number, action: ProCoreActionType) => {
        return record.deploymentKind?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.type'}),
      key: `type`,
      renderText: (text: any, record: Entity, index: number, action: ProCoreActionType) => {
        return record.type?.label
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      key: `remark`,
      dataIndex: 'remark',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.createTime'}),
      key: `createTime`,
      dataIndex: 'createTime',
    },
    {
      title: intl.formatMessage({id: 'app.common.data.updateTime'}),
      key: `updateTime`,
      dataIndex: 'updateTime',
    },
    {
      title: '操作',
      valueType: 'option',
      render: () => [
        <a target="_blank" rel="noopener noreferrer" key="link">
          按钮
        </a>
      ],
    },
  ]

  const items = [
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesJobDetailYAMLWeb data={urlParams.state}/>
    },
  ]
  return (
    <PageContainer>
      <ProDescriptions
        column={3}
        dataSource={urlParams.state}
        columns={descriptionColumns}
        // extra={buttons}
      />
      <Tabs
        type="card"
        items={items}
      />
    </PageContainer>
  );
}

export default FlinkKubernetesJobDetailWeb;
