import {useAccess, useIntl, useLocation} from "umi";
import React from "react";
import {Button, message, Popconfirm, Tabs} from "antd";
import FlinkKubernetesJobDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/YAML";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {PageContainer, ProDescriptions} from "@ant-design/pro-components";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  OrderedListOutlined,
  PauseOutlined
} from "@ant-design/icons";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

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
      renderText: (text: any, record: WsFlinkKubernetesJob, index: number, action: ProCoreActionType) => {
        return record.executionMode?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.deploymentKind'}),
      key: `deploymentKind`,
      renderText: (text: any, record: WsFlinkKubernetesJob, index: number, action: ProCoreActionType) => {
        return record.deploymentKind?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.type'}),
      key: `type`,
      renderText: (text: any, record: WsFlinkKubernetesJob, index: number, action: ProCoreActionType) => {
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
        <div>
          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsFlinkKubernetesJobService.deploy({wsFlinkKubernetesJobId: urlParams.state.id}).then(response => {
                message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
              })
            }}
          >
            <Button
              type="default"
              icon={<CaretRightOutlined/>}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy'})}
            </Button>
          </Popconfirm>
          <Button
            type="default"
            icon={<PauseOutlined/>}
            disabled
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.suspend'})}
          </Button>

          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsFlinkKubernetesJobService.shutdown(urlParams.state).then(response => {
                message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
              })
            }}
          >
            <Button
              icon={<CloseOutlined/>}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.shutdown'})}
            </Button>
          </Popconfirm>
        </div>,

        <div>
          <Button
            type="default"
            icon={<CameraOutlined/>}
            disabled
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint'})}
          </Button>
        </div>,

        <div>
          <Button
            type="default"
            icon={<DashboardOutlined/>}
            onClick={() => WsFlinkKubernetesJobService.flinkui(urlParams.state)}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.flinkui'})}
          </Button>
          <Button
            type="default"
            icon={<AreaChartOutlined/>}
            disabled
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.metrics'})}
          </Button>
          <Button
            type="default"
            icon={<OrderedListOutlined/>}
            disabled
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.logs'})}
          </Button>
        </div>
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
