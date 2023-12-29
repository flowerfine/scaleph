import {connect, useAccess, useIntl, useLocation} from "umi";
import React, {useEffect, useState} from "react";
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
  RedoOutlined
} from "@ant-design/icons";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import FlinkKubernetesJobDeployForm from "@/pages/Project/Workspace/Kubernetes/Job/Detail/JobDeployForm";
import FlinkKubernetesJobShutdownForm from "@/pages/Project/Workspace/Kubernetes/Job/Detail/JobShutdownForm";
import FlinkKubernetesJobDetailInstanceListWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/InstanceList";
import FlinkKubernetesJobDetailSavepointWeb from "@/pages/Project/Workspace/Kubernetes/Job/Detail/Savepoint";

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
          <Button type="default">
            {props.jobDetail.job?.jobInstance?.jobState?.label}
          </Button>

        </div>,
        <div>
          <Button
            type="default"
            icon={<CaretRightOutlined/>}
            onClick={() => {
              setJobDeployFormData({visiable: true, data: props.jobDetail.job})
            }}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy'})}
          </Button>

          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsFlinkKubernetesJobService.restart(props.jobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<RedoOutlined/>}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.restart'})}
            </Button>
          </Popconfirm>

          <Button
            icon={<CloseOutlined/>}
            onClick={() => {
              setJobShutdownFormData({visiable: true, data: props.jobDetail.job})
            }}
          >
            {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.shutdown'})}
          </Button>
        </div>,

        <div>
          <Popconfirm
            title={intl.formatMessage({id: 'app.common.operate.submit.confirm.title'})}
            onConfirm={() => {
              WsFlinkKubernetesJobService.triggerSavepoint(props.jobDetail.job.jobInstance.id).then(response => {
                if (response.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.submit.success'}));
                }
              })
            }}
          >
            <Button
              type="default"
              icon={<CameraOutlined/>}
            >
              {intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint'})}
            </Button>
          </Popconfirm>
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
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.instanceList'}),
      key: 'instanceList',
      children: <FlinkKubernetesJobDetailInstanceListWeb data={props.jobDetail.job}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.savepoint'}),
      key: 'savepoint',
      children: <FlinkKubernetesJobDetailSavepointWeb data={props.jobDetail.job}/>
    },
  ]
  return (
    <div>
      <PageContainer title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail'})}>
        <ProDescriptions
          column={3}
          dataSource={props.jobDetail.job}
          columns={descriptionColumns}
        />
        <Tabs
          type="card"
          items={items}
        />
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

const mapModelToProps = ({jobDetail}: any) => ({jobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailWeb);
