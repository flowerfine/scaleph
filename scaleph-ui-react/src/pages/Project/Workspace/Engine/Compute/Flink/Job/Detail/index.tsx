import React, {useEffect, useState} from "react";
import {Button, message, Popconfirm, Tabs} from "antd";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  DashboardOutlined,
  OrderedListOutlined,
  RedoOutlined
} from "@ant-design/icons";
import {ProDescriptionsItemProps} from "@ant-design/pro-descriptions";
import {PageContainer, ProCard, ProDescriptions} from "@ant-design/pro-components";
import {connect, useAccess, useIntl, useLocation} from "@umijs/max";
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import FlinkKubernetesJobDetailYAMLWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/YAML";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import FlinkKubernetesJobDeployForm from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/JobDeployForm";
import FlinkKubernetesJobShutdownForm from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/JobShutdownForm";
import FlinkKubernetesJobDetailInstanceListWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/InstanceList";
import FlinkKubernetesJobDetailSavepointWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Savepoint";
import FlinkKubernetesJobDetailOverviewWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview";
import {FlinkJobState, ResourceLifecycleState} from "@/constants/enum";

const FlinkKubernetesJobDetailWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const urlParams = useLocation()
  const [jobDeployFormData, setJobDeployFormData] = useState<{
    visiable: boolean;
    data: WsFlinkKubernetesJob;
  }>({visiable: false, data: {}});
  const [jobShutdownFormData, setJobShutdownFormData] = useState<{
    visiable: boolean;
    data: WsFlinkKubernetesJob;
  }>({visiable: false, data: {}});

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

  const refreshJob = (id: number | undefined) => {
    props.dispatch({
      type: 'flinkKubernetesJobDetail/queryJob',
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
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      valueType: 'option',
      render: () => [
        <div>
          <Button type="text">
            {props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.label}
          </Button>
        </div>,

        <div>
          <Button
            type="default"
            icon={<CaretRightOutlined/>}
            disabled={props.flinkKubernetesJobDetail.job?.jobInstance?.state}
            onClick={() => {
              setJobDeployFormData({visiable: true, data: props.flinkKubernetesJobDetail.job})
            }}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy'})}
          </Button>

          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
              && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            onConfirm={() => {
              WsFlinkKubernetesJobService.restart(props.flinkKubernetesJobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<RedoOutlined/>}
              disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
                && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.restart'})}
            </Button>
          </Popconfirm>

          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
              && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            onConfirm={() => {
              WsFlinkKubernetesJobService.suspend(props.flinkKubernetesJobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<RedoOutlined/>}
              disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
                && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.suspend'})}
            </Button>
          </Popconfirm>

          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.SUSPENDED)}
            onConfirm={() => {
              WsFlinkKubernetesJobService.resume(props.flinkKubernetesJobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<RedoOutlined/>}
              disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.SUSPENDED)}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.resume'})}
            </Button>
          </Popconfirm>

          <Button
            icon={<CloseOutlined/>}
            disabled={!props.flinkKubernetesJobDetail.job?.jobInstance?.state}
            onClick={() => {
              setJobShutdownFormData({visiable: true, data: props.flinkKubernetesJobDetail.job})
            }}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.shutdown'})}
          </Button>
        </div>,

        <div>
          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
              && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            onConfirm={() => {
              WsFlinkKubernetesJobService.triggerSavepoint(props.flinkKubernetesJobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<CameraOutlined/>}
              disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE
                && props.flinkKubernetesJobDetail.job?.jobInstance?.jobState?.value == FlinkJobState.RUNNING)}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.triggerSavepoint'})}
            </Button>
          </Popconfirm>
        </div>,

        <div>
          <Button
            type="default"
            icon={<DashboardOutlined/>}
            disabled={!(props.flinkKubernetesJobDetail.job?.jobInstance?.state?.value == ResourceLifecycleState.STABLE)}
            onClick={() => WsFlinkKubernetesJobService.flinkui(props.flinkKubernetesJobDetail.job.jobInstance.id)}
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
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview'}),
      key: 'overview',
      children: <FlinkKubernetesJobDetailOverviewWeb/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint'}),
      key: 'savepoint',
      children: <FlinkKubernetesJobDetailSavepointWeb data={props.flinkKubernetesJobDetail.job}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesJobDetailYAMLWeb data={props.flinkKubernetesJobDetail.job}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList'}),
      key: 'instanceList',
      children: <FlinkKubernetesJobDetailInstanceListWeb data={props.flinkKubernetesJobDetail.job}/>
    },
  ]
  return (
    <div>
      <PageContainer title={false}>
        <ProCard.Group direction={"column"}>
          <ProCard>
            <ProDescriptions
              column={3}
              bordered={true}
              dataSource={props.flinkKubernetesJobDetail.job}
              columns={descriptionColumns}
            />
          </ProCard>
          <ProCard>
            <Tabs
              type="card"
              items={items}
            />
          </ProCard>
        </ProCard.Group>
      </PageContainer>
      {jobDeployFormData.visiable && (
        <FlinkKubernetesJobDeployForm
          visible={jobDeployFormData.visiable}
          onCancel={() => {
            setJobDeployFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setJobDeployFormData({visiable: visiable, data: {}});
          }}
          data={jobDeployFormData.data}
        />
      )}

      {jobShutdownFormData.visiable && (
        <FlinkKubernetesJobShutdownForm
          visible={jobShutdownFormData.visiable}
          onCancel={() => {
            setJobShutdownFormData({visiable: false, data: {}});
          }}
          onVisibleChange={(visiable) => {
            setJobShutdownFormData({visiable: visiable, data: {}});
          }}
          data={jobShutdownFormData.data}
        />
      )}
    </div>
  );
}

const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailWeb);
