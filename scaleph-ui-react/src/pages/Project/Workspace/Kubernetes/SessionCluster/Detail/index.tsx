import {connect, useAccess, useIntl, useLocation} from "umi";
import React, {useEffect} from "react";
import {PageContainer, ProDescriptions} from "@ant-design/pro-components";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {Button, message, Popconfirm, Space, Tabs} from "antd";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  OrderedListOutlined,
  PauseOutlined
} from "@ant-design/icons";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import FlinkKubernetesSessinClusterDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/YAML";
import FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/Configuration";
import FlinkKubernetesSessinClusterDetailOptionsWeb
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/Options";
import FlinkKubernetesSessinClusterDetailLogWeb from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/Log";
import FlinkKubernetesSessinClusterDetailPodTemplateWeb
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/PodTemplate";

const FlinkKubernetesSessionClusterDetailWeb: React.FC = (props: any) => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();

  useEffect(() => {
    const data = urlParams.state as WsFlinkKubernetesSessionCluster
    refreshSessionCluster(data.id)
    let timer = setInterval(() => {
      refreshSessionCluster(data.id)
    }, 3000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  const refreshSessionCluster = (id: number) => {
    props.dispatch({
      type: 'flinkKubernetesSessionClusterDetail/querySessionCluster',
      payload: id
    })
  }

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
        <Popconfirm
          title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
          disabled={props.flinkKubernetesSessionClusterDetail.sessionCluster?.state}
          onConfirm={() => {
            WsFlinkKubernetesSessionClusterService.deploy(props.flinkKubernetesSessionClusterDetail.sessionCluster).then(response => {
              message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
            })
          }}
        >
          <Button
            type="default"
            icon={<CaretRightOutlined/>}
            disabled={props.flinkKubernetesSessionClusterDetail.sessionCluster?.state}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.deploy'})}
          </Button>
        </Popconfirm>

        <Button
          type="default"
          icon={<PauseOutlined/>}
          disabled
        >
          {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.suspend'})}
        </Button>

        <Popconfirm
          title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
          disabled={!props.flinkKubernetesSessionClusterDetail.sessionCluster?.state}
          onConfirm={() => {
            WsFlinkKubernetesSessionClusterService.shutdown(props.flinkKubernetesSessionClusterDetail.sessionCluster).then(response => {
              message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
            })
          }}
        >
          <Button
            type="default"
            icon={<CloseOutlined/>}
            disabled={!props.flinkKubernetesSessionClusterDetail.sessionCluster?.state}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.shutdown'})}
          </Button>
        </Popconfirm>
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
          disabled={props.flinkKubernetesSessionClusterDetail.sessionCluster?.state?.value != 'STABLE'}
          onClick={() => WsFlinkKubernetesSessionClusterService.flinkui(props.flinkKubernetesSessionClusterDetail.sessionCluster)}
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
      children: <FlinkKubernetesSessinClusterDetailOptionsWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.configuration'}),
      key: 'configuration',
      children: <FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.log'}),
      key: 'log',
      children: <FlinkKubernetesSessinClusterDetailLogWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.pod'}),
      key: 'pod',
      children: <FlinkKubernetesSessinClusterDetailPodTemplateWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.detail.tab.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesSessinClusterDetailYAMLWeb/>
    },
  ]

  return (
    <PageContainer title={false}>
      <ProDescriptions
        column={2}
        dataSource={props.flinkKubernetesSessionClusterDetail.sessionCluster}
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

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterDetailWeb);
