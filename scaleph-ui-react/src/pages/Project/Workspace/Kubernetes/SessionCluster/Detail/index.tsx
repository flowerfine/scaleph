import {useAccess, useIntl, useLocation} from "umi";
import React, {useRef} from "react";
import {ActionType, PageContainer, ProDescriptions} from "@ant-design/pro-components";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {Button, Space, Tabs} from "antd";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  OrderedListOutlined,
  PauseOutlined
} from "@ant-design/icons";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import FlinkKubernetesSessinClusterDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/YAML";

const FlinkKubernetesSessionClusterDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const params = urlParams.state as WsFlinkKubernetesSessionCluster;

  const descriptionColumns: ProDescriptionsItemProps<WsFlinkKubernetesSessionCluster>[] = [
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.name'}),
      key: `name`,
      dataIndex: 'name',
    },
    {
      title: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.namespace'}),
      key: `namespace`,
      dataIndex: 'namespace'
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
  ]

  const buttons: React.ReactNode =
    <Space>
      <div>
        <Button
          type="default"
          icon={<CaretRightOutlined/>}
          onClick={() => WsFlinkKubernetesSessionClusterService.deploy(params)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.deploy'})}
        </Button>
        <Button
          type="default"
          icon={<PauseOutlined/>}>
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.suspend'})}
        </Button>
        <Button
          type="default"
          icon={<CloseOutlined/>}
          onClick={() => WsFlinkKubernetesSessionClusterService.shutdown(params)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.shutdown'})}
        </Button>
      </div>

      <div>
        <Button
          type="default"
          icon={<CameraOutlined/>}>
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.savepoint'})}
        </Button>
      </div>

      <div>
        <Button
          type="default"
          icon={<DashboardOutlined/>}
          onClick={() => WsFlinkKubernetesSessionClusterService.flinkui(params)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.flinkui'})}
        </Button>
        <Button
          type="default"
          icon={<AreaChartOutlined/>}>
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.metrics'})}
        </Button>
        <Button
          type="default"
          icon={<OrderedListOutlined/>}>
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.logs'})}
        </Button>
      </div>
    </Space>

  const items = [
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesSessinClusterDetailYAMLWeb data={params}/>
    },
  ]

  return (
    <PageContainer>
      <ProDescriptions
        column={2}
        dataSource={params}
        columns={descriptionColumns}
        extra={buttons}
      />
      <Tabs items={items}/>
    </PageContainer>
  );
}

export default FlinkKubernetesSessionClusterDetailWeb;
