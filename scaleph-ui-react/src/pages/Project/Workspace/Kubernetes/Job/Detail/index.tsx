import {connect, useAccess, useIntl, useLocation} from "umi";
import React, {useEffect} from "react";
import {Button, message, Popconfirm, Tabs} from "antd";
import FlinkKubernetesJobDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/YAML";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {PageContainer, ProDescriptions} from "@ant-design/pro-components";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
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

const FlinkKubernetesJobDetailWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const urlParams = useLocation()

  useEffect(() => {
    const data = urlParams.state as WsFlinkKubernetesJob
    refreshJob(data.id)
    let timer = setInterval(() => {
      refreshJob(data.id)
    }, 3000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  const refreshJob = (id: number) => {
    props.dispatch({
      type: 'jobDetail/queryJob',
      payload: id
    })
  }

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
              WsFlinkKubernetesJobService.deploy({wsFlinkKubernetesJobId: props.jobDetail.job.id}).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
                refreshJob(props.jobDetail.job.id)
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
              WsFlinkKubernetesJobService.shutdown({id: props.jobDetail.job.jobInstance.id}).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
                refreshJob(props.jobDetail.job.id)
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
            onClick={() => WsFlinkKubernetesJobService.flinkui(props.jobDetail.job.jobInstance.id)}
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
      children: <FlinkKubernetesJobDetailYAMLWeb data={props.jobDetail.job}/>
    },
  ]
  return (
    <PageContainer>
      <ProDescriptions
        column={3}
        dataSource={props.jobDetail.job}
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

const mapModelToProps = ({jobDetail}: any) => ({jobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailWeb);
