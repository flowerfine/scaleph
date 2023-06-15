import {useAccess, useIntl, useLocation} from "umi";
import React, {useState} from "react";
import {PageContainer, ProDescriptions} from "@ant-design/pro-components";
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
import FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/Configuration";
import FlinkKubernetesSessinClusterDetailOptionsWeb
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/Options";

const FlinkKubernetesSessionClusterDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();

  const [data, setData] = useState<WsFlinkKubernetesSessionCluster>(urlParams.state as WsFlinkKubernetesSessionCluster)

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
          disabled={data.state}
          onClick={() => WsFlinkKubernetesSessionClusterService.deploy(data)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.deploy'})}
        </Button>
        <Button
          type="default"
          icon={<PauseOutlined/>}
          disabled
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.suspend'})}
        </Button>
        <Button
          type="default"
          icon={<CloseOutlined/>}
          disabled={!data.state}
          onClick={() => WsFlinkKubernetesSessionClusterService.shutdown(data)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.shutdown'})}
        </Button>
      </div>

      <div>
        <Button
          type="default"
          icon={<CameraOutlined/>}
          disabled
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.savepoint'})}
        </Button>
      </div>

      <div>
        <Button
          type="default"
          icon={<DashboardOutlined/>}
          disabled={data.state?.value != 'STABLE'}
          onClick={() => WsFlinkKubernetesSessionClusterService.flinkui(data)}
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.flinkui'})}
        </Button>
        <Button
          type="default"
          icon={<AreaChartOutlined/>}
          disabled
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.metrics'})}
        </Button>
        <Button
          type="default"
          icon={<OrderedListOutlined/>}
          disabled
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.logs'})}
        </Button>
      </div>
    </Space>

  const items = [
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.options'}),
      key: 'options',
      children: <FlinkKubernetesSessinClusterDetailOptionsWeb data={data}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.configuration'}),
      key: 'configuration',
      children: <FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb data={data}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesSessinClusterDetailYAMLWeb data={data}/>
    },
  ]

  return (
    <PageContainer>
      <ProDescriptions
        column={2}
        dataSource={data}
        columns={descriptionColumns}
        extra={buttons}
      />
      <Tabs
        type="card"
        items={items}
      />
    </PageContainer>
  );
}

export default FlinkKubernetesSessionClusterDetailWeb;
